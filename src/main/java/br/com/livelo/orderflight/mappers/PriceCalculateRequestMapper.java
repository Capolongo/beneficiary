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
    static final String TYPE_FLIGHT = "flight";
    static final String TYPE_TAX = "TAX";

    @Mapping(target = "travelInfo", source = "orderEntity", qualifiedByName = "buildTravelInfo")
    @Mapping(target = "items", source = "orderEntity", qualifiedByName = "buildListPricingCalculateItems")
    PricingCalculateRequest toPricingCalculateRequest(OrderEntity orderEntity);

    @Named("buildTravelInfo")
    default PricingCalculateTravelInfo buildTravelInfo(OrderEntity orderEntity) {

        return orderEntity.getItems().stream()
                .filter(item -> TYPE_FLIGHT.equals(item.getProductId()))
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
                .filter(item -> TYPE_FLIGHT.equals(item.getProductId()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));


        for (SegmentEntity segmentEntity : orderItemEntity.getSegments()) {
            var airline = segmentEntity.getFlightsLegs().stream().findFirst().map(FlightLegEntity::getAirline).orElse(null);
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
                            // RECUPERAR IATA
                            .airline(PricingCalculateAirline.builder().description(airline).build())
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
                .managedBy(PricingCalculateManagedBy.builder().description(flightLegEntity.getManagedBy()).build())
                .operatedBy(PricingCalculateOperatedBy.builder().description(flightLegEntity.getAirline()).build())
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
        var totalTaxes = BigDecimal.ZERO;
        var totalFlights = BigDecimal.ZERO;
        return PricingCalculatePrice.builder()
                .amount(orderEntity.getPrice().getPartnerAmount())
                .currency("BRL")
                .pricesDescription(buildPricingCalculatePricesDescription(orderEntity, totalTaxes, totalFlights))
                .flight(PricingCalculateFlight.builder().amount(totalFlights).build())
                .taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
                .build();
    }

    default PricingCalculatePricesDescription buildPricingCalculatePricesDescription(OrderEntity orderEntity, final BigDecimal totalTaxes, final BigDecimal totalFlights) {
        return PricingCalculatePricesDescription.builder()
                .flights(buildPricingCalculateFlight(orderEntity, totalFlights))
                .taxes(buildPricingCalculatePriceTaxes(orderEntity, totalTaxes))
                .build();
    }

    default List<PricingCalculateFlight> buildPricingCalculateFlight(OrderEntity orderEntity, BigDecimal totalFlights) {
        List<PricingCalculateFlight> pricingCalculateFlight = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (orderItemEntity.getProductId().equals(TYPE_FLIGHT)) {
                totalFlights = totalFlights.add(orderItemEntity.getPrice().getPartnerAmount());
                if (orderItemEntity.getTravelInfo().getAdultQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("ADULT")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getAdultQuantity())
                            .build());
                }
                if (orderItemEntity.getTravelInfo().getChildQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("CHILD")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getChildQuantity())
                            .build());
                }

                if (orderItemEntity.getTravelInfo().getBabyQuantity() > 0) {
                    pricingCalculateFlight.add(PricingCalculateFlight.builder()
                            .passengerType("BABY")
                            .amount(orderItemEntity.getPrice().getPartnerAmount())
                            .passengerCount(orderItemEntity.getTravelInfo().getBabyQuantity())
                            .build());
                }
            }
        }

        return orderEntity.getItems().stream()
                .map(item -> {
                    if (item.getProductId().equals(TYPE_FLIGHT)) {
                        if (item.getTravelInfo().getAdultQuantity() > 0) {
                            return PricingCalculateFlight.builder()
                                    .passengerType("ADULT")
                                    .amount(item.getPrice().getPartnerAmount())
                                    .passengerCount(item.getTravelInfo().getAdultQuantity())
                                    .build();
                        }
                        if (item.getTravelInfo().getChildQuantity() > 0) {
                            return PricingCalculateFlight.builder()
                                    .passengerType("CHILD")
                                    .amount(item.getPrice().getPartnerAmount())
                                    .passengerCount(item.getTravelInfo().getChildQuantity())
                                    .build();
                        }

                        if (item.getTravelInfo().getBabyQuantity() > 0) {
                            return PricingCalculateFlight.builder()
                                    .passengerType("BABY")
                                    .amount(item.getPrice().getPartnerAmount())
                                    .passengerCount(item.getTravelInfo().getBabyQuantity())
                                    .build();
                        }
                    }
                    return null;
                }).toList();
    }


    default List<PricingCalculateTaxes> buildPricingCalculatePriceTaxes(OrderEntity orderEntity, BigDecimal totalTaxes) {
        List<PricingCalculateTaxes> pricingCalculateTaxes = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderEntity.getItems()) {
            if (orderItemEntity.getProductId().equals("flight_tax")) {
                totalTaxes = totalTaxes.add(orderItemEntity.getPrice().getPartnerAmount());
                pricingCalculateTaxes.add(PricingCalculateTaxes.builder()
                        .type(TYPE_TAX)
                        .amount(totalTaxes)
                        .build());
            }
        }
        return pricingCalculateTaxes;
    }

}
