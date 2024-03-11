package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.pricing.request.*;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.enuns.Partner;
import br.com.livelo.orderflight.exception.OrderFlightException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@Mapper(componentModel = "spring")
public interface PriceCalculateRequestMapper {
    String STAGE_JOURNEY = "RESERVATION";

    @Mapping(target = "travelInfo", source = "orderEntity", qualifiedByName = "buildTravelInfo")
    @Mapping(target = "items", source = "orderEntity", qualifiedByName = "buildListPricingCalculateItems")
    PricingCalculateRequest toPricingCalculateRequest(OrderEntity orderEntity);

    @Named("buildTravelInfo")
    default PricingCalculateTravelInfo buildTravelInfo(OrderEntity orderEntity) {
        var partner = Partner.findByName(orderEntity.getPartnerCode());

        return orderEntity.getItems().stream()
                .filter(item -> partner.getSkuFlight().equals(item.getSkuId()))
                .findFirst()
                .map(item -> PricingCalculateTravelInfo.builder()
                        .type(item.getTravelInfo().getType())
                        .adultQuantity(item.getTravelInfo().getAdultQuantity())
                        .babyQuantity(item.getTravelInfo().getBabyQuantity())
                        .childQuantity(item.getTravelInfo().getChildQuantity())
                        .typeClass(item.getTravelInfo().getTypeClass())
                        .stageJourney(STAGE_JOURNEY)
                        .build())
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Travel info not found on order!"));
    }

    @Named("buildListPricingCalculateItems")
    default List<PricingCalculateItem> buildListPricingCalculateItems(OrderEntity orderEntity) {
        var partner = Partner.valueOf(orderEntity.getPartnerCode());

        var flightType = orderEntity.getItems().stream()
                .filter(item -> partner.getSkuFlight().equals(item.getSkuId()))
                .findFirst()
                .map(item -> item.getTravelInfo().getType())
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "ITEM FLIGHT NOT FOUND ON ORDER ITEMS!"));

        return List.of(PricingCalculateItem.builder()
                .id(orderEntity.getCommerceOrderId())
                .flightType(flightType)
                .price(buildPricingCalculatePrice(orderEntity, partner.getSkuFlight(), partner.getSkuTax()))
                .segments(buildSegments(orderEntity, partner.getSkuFlight()))
                .build());
    }

    default List<PricingCalculateSegment> buildSegments(OrderEntity orderEntity, String sku) {
        List<PricingCalculateSegment> pricingCalculateSegment = new ArrayList<>();
        var orderItemEntity = orderEntity.getItems()
                .stream()
                .filter(item -> sku.equals(item.getSkuId()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));


        for (SegmentEntity segmentEntity : orderItemEntity.getSegments()) {
            var flightLeg = segmentEntity.getFlightsLegs().stream()
                    .findFirst()
                    .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Airline not found in flight legs!"));

            pricingCalculateSegment.add(
                    PricingCalculateSegment.builder()
                            .step(Integer.valueOf(segmentEntity.getStep()))
                            .originIata(segmentEntity.getOriginIata())
                            .originDescription(segmentEntity.getOriginDescription())
                            .originDate(String.valueOf(segmentEntity.getDepartureDate()))
                            .destinationIata(segmentEntity.getDestinationIata())
                            .destinationDescription(segmentEntity.getDestinationDescription())
                            .destinationDate(String.valueOf(segmentEntity.getArrivalDate()))
                            .numberOfStops(segmentEntity.getStops())
                            .flightDuration(segmentEntity.getFlightDuration())
                            .airline(PricingCalculateAirline.builder().iata(flightLeg.getAirlineManagedByIata()).description(flightLeg.getAirlineManagedByDescription()).build())
                            .flightClass(orderItemEntity.getTravelInfo().getTypeClass())
                            .luggages(buildLuggages(segmentEntity))
                            .cancellationRules(buildCancellationRules(segmentEntity))
                            .changeRules(buildCalculateChangeRules(segmentEntity))
                            .flightsLegs(buildFlightsLegs(segmentEntity, orderItemEntity.getTravelInfo().getTypeClass()))
                            .build()
            );
        }
        return pricingCalculateSegment;
    }

    default List<PricingCalculateFlightsLeg> buildFlightsLegs(SegmentEntity segmentEntity, String typeClass) {
        List<PricingCalculateFlightsLeg> flightsLegs = new ArrayList<>();
        for (FlightLegEntity flightLegEntity : segmentEntity.getFlightsLegs()) {
            flightsLegs.add(
                    PricingCalculateFlightsLeg.builder()
                            .airline(buildPricingCalculateAirline(flightLegEntity))
                            .flightNumber(flightLegEntity.getFlightNumber())
                            .flightDuration(flightLegEntity.getFlightDuration())
                            .originIata(flightLegEntity.getOriginIata())
                            .timeToWait(String.valueOf(flightLegEntity.getTimeToWait()))
                            .originDescription(flightLegEntity.getOriginDescription())
                            .departureDate(String.valueOf(flightLegEntity.getDepartureDate()))
                            .destinationIata(flightLegEntity.getDestinationIata())
                            .destinationDescription(flightLegEntity.getDestinationDescription())
                            .arrivalDate(String.valueOf(flightLegEntity.getArrivalDate()))
                            .flightClass(typeClass)
                            .build()
            );
        }
        return flightsLegs;
    }

    default List<PricingCalculateLuggage> buildLuggages(SegmentEntity segmentEntity) {
        List<PricingCalculateLuggage> luggages = new ArrayList<>();
        for (LuggageEntity luggageEntity : segmentEntity.getLuggages()) {
            luggages.add(PricingCalculateLuggage.builder()
                    .description(luggageEntity.getDescription())
                    .type(luggageEntity.getType())
                    .quantity(null)
                    .weight(null)
                    .measurement(null)
                    .build());
        }
        return luggages;
    }

    default PricingCalculateAirline buildPricingCalculateAirline(FlightLegEntity flightLegEntity) {
        return PricingCalculateAirline.builder()
                .iata(flightLegEntity.getAirlineManagedByIata())
                .description(flightLegEntity.getAirlineManagedByDescription())
                .managedBy(PricingCalculateManagedBy.builder()
                        .iata(flightLegEntity.getAirlineManagedByIata())
                        .description(flightLegEntity.getAirlineManagedByDescription())
                        .build())
                .operatedBy(PricingCalculateOperatedBy.builder()
                        .iata(flightLegEntity.getAirlineOperatedByIata())
                        .description(flightLegEntity.getAirlineOperatedByDescription())
                        .build())
                .build();

    }

    default List<PricingCalculateCancellationRule> buildCancellationRules(SegmentEntity segmentEntity) {
        List<PricingCalculateCancellationRule> cancellationRules = new ArrayList<>();
        for (CancelationRuleEntity cancelationRuleEntity : segmentEntity.getCancelationRules()) {
            cancellationRules.add(PricingCalculateCancellationRule.builder()
                    .description(cancelationRuleEntity.getDescription())
                    .build()
            );
        }
        return cancellationRules;
    }

    default List<PricingCalculateChangeRule> buildCalculateChangeRules(SegmentEntity segmentEntity) {
        List<PricingCalculateChangeRule> changeRules = new ArrayList<>();
        for (ChangeRuleEntity changeRuleEntity : segmentEntity.getChangeRules()) {
            changeRules.add(PricingCalculateChangeRule.builder()
                    .description(changeRuleEntity.getDescription())
                    .build()
            );
        }
        return changeRules;
    }

    default PricingCalculatePrice buildPricingCalculatePrice(OrderEntity orderEntity, String skuFlight, String skuTax) {
        var pricingCalculatePriceDescription = buildPricingCalculatePricesDescription(orderEntity, skuFlight, skuTax);

        var totalFlights = pricingCalculatePriceDescription.getFlights().stream()
                .map(PricingCalculateFlight::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var totalTaxes = pricingCalculatePriceDescription.getTaxes().stream()
                .map(PricingCalculateTaxes::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PricingCalculatePrice.builder()
                .amount(orderEntity.getPrice().getPartnerAmount())
                .currency("BRL")
                .pricesDescription(pricingCalculatePriceDescription)
                .flight(PricingCalculateFlight.builder().amount(totalFlights).build())
                .taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
                .build();
    }

    default PricingCalculatePricesDescription buildPricingCalculatePricesDescription(OrderEntity orderEntity, String skuFlight, String skuTax) {
        return PricingCalculatePricesDescription.builder()
                .flights(buildPricingCalculateFlight(orderEntity, skuFlight))
                .taxes(buildPricingCalculatePriceTaxes(orderEntity, skuTax))
                .build();
    }

    default List<PricingCalculateFlight> buildPricingCalculateFlight(OrderEntity orderEntity, String sku) {
        List<PricingCalculateFlight> pricingCalculateFlight = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (sku.equals(orderItemEntity.getSkuId())) {
                if (orderItemEntity.getTravelInfo().getAdultQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("ADT")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getAdultQuantity())
                            .build());
                }
                if (orderItemEntity.getTravelInfo().getChildQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("CHD")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getChildQuantity())
                            .build());
                }

                if (orderItemEntity.getTravelInfo().getBabyQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("INF")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getBabyQuantity())
                            .build());
                }
            }
        }
        return pricingCalculateFlight;
    }


    default List<PricingCalculateTaxes> buildPricingCalculatePriceTaxes(OrderEntity orderEntity, String sku) {
        List<PricingCalculateTaxes> pricingCalculateTaxes = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (sku.equals(orderItemEntity.getSkuId())) {
                pricingCalculateTaxes.add(PricingCalculateTaxes.builder()
                        .type("TAX")
                        .amount(orderItemEntity.getPrice().getPartnerAmount())
                        .build());
            }
        }
        return pricingCalculateTaxes;
    }

}
