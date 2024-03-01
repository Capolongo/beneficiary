package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.pricing.request.*;
import br.com.livelo.orderflight.domain.entity.*;
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
    String TYPE_FLIGHT = "cvc_flight";
    String TYPE_TAX = "cvc_flight_tax";

    @Mapping(target = "travelInfo", source = "orderEntity", qualifiedByName = "buildTravelInfo")
    @Mapping(target = "items", source = "orderEntity", qualifiedByName = "buildListPricingCalculateItems")
    PricingCalculateRequest toPricingCalculateRequest(OrderEntity orderEntity);

    @Named("buildTravelInfo")
    default PricingCalculateTravelInfo buildTravelInfo(OrderEntity orderEntity) {

        return orderEntity.getItems().stream()
                .filter(item -> TYPE_FLIGHT.equals(item.getSkuId()))
                .findFirst()
                .map(item -> PricingCalculateTravelInfo.builder()
                        .type(item.getTravelInfo().getType())
                        .adultQuantity(item.getTravelInfo().getAdultQuantity())
                        .babyQuantity(item.getTravelInfo().getBabyQuantity())
                        .childQuantity(item.getTravelInfo().getChildQuantity())
                        .typeClass(item.getTravelInfo().getTypeClass())
                        .stageJourney("RESERVATION")
                        .build()).orElse(null);
    }

    @Named("buildListPricingCalculateItems")
    default List<PricingCalculateItem> buildListPricingCalculateItems(OrderEntity orderEntity) {
        return List.of(PricingCalculateItem.builder()
                .id(orderEntity.getCommerceOrderId())
                .flightType(TYPE_FLIGHT)
                .price(buildPricingCalculatePrice(orderEntity))
                .segments(buildSegments(orderEntity))
                .build());
    }

    default List<PricingCalculateSegment> buildSegments(OrderEntity orderEntity) {
        List<PricingCalculateSegment> pricingCalculateSegment = new ArrayList<>();
        var orderItemEntity = orderEntity.getItems()
                .stream()
                .filter(item -> TYPE_FLIGHT.equals(item.getSkuId()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));


        for (SegmentEntity segmentEntity : orderItemEntity.getSegments()) {
            var airline = segmentEntity.getFlightsLegs().stream()
                    .findFirst()
                    .map(FlightLegEntity::getAirline)
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
                            .airline(PricingCalculateAirline.builder().iata(airline.getManagedBy().getIata()).description(airline.getManagedBy().getDescription()).build())
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

    default List<PricingCalculateFlightsLeg> buildFlightsLegs(SegmentEntity segmentEntity, String typeClassParam) {
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
                            .flightClass(typeClassParam)
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
                .description(flightLegEntity.getManagedBy())
                .managedBy(PricingCalculateManagedBy.builder()
                        .iata(flightLegEntity.getAirline().getManagedBy().getIata())
                        .description(flightLegEntity.getAirline().getManagedBy().getDescription())
                        .build())
                .operatedBy(PricingCalculateOperatedBy.builder()
                        .iata(flightLegEntity.getAirline().getOperatedBy().getIata())
                        .description(flightLegEntity.getAirline().getOperatedBy().getDescription())
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

    default PricingCalculatePrice buildPricingCalculatePrice(OrderEntity orderEntity) {
        var pricingCalculatePriceDescription = buildPricingCalculatePricesDescription(orderEntity);

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

    default PricingCalculatePricesDescription buildPricingCalculatePricesDescription(OrderEntity orderEntity) {
        return PricingCalculatePricesDescription.builder()
                .flights(buildPricingCalculateFlight(orderEntity))
                .taxes(buildPricingCalculatePriceTaxes(orderEntity))
                .build();
    }

    default List<PricingCalculateFlight> buildPricingCalculateFlight(OrderEntity orderEntity) {
        List<PricingCalculateFlight> pricingCalculateFlight = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (orderItemEntity.getSkuId().equals(TYPE_FLIGHT)) {
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


    default List<PricingCalculateTaxes> buildPricingCalculatePriceTaxes(OrderEntity orderEntity) {
        List<PricingCalculateTaxes> pricingCalculateTaxes = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (orderItemEntity.getSkuId().equals(TYPE_TAX)) {
                pricingCalculateTaxes.add(PricingCalculateTaxes.builder()
                        .type("TAX")
                        .amount(orderItemEntity.getPrice().getPartnerAmount())
                        .build());
            }
        }
        return pricingCalculateTaxes;
    }

}
