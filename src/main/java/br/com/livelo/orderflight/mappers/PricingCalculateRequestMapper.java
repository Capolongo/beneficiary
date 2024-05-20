package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.request.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PricingCalculateRequestMapper {

    @Mapping(target = "travelInfo", expression = "java(buildTravelInfo(getItemTypeFlight(partnerReservationResponse)))")
    @Mapping(target = "items", expression = "java(buildListPricingCalculateItems(partnerReservationResponse, getItemTypeFlight(partnerReservationResponse), commerceOrderId))")
    PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse, String commerceOrderId);

    @Mappings({
            @Mapping(source = "travelInfo.type", target = "type"),
            @Mapping(source = "travelInfo.adt", target = "adt"),
            @Mapping(source = "travelInfo.chd", target = "chd"),
            @Mapping(source = "travelInfo.inf", target = "inf"),
            @Mapping(source = "travelInfo.cabinClass", target = "cabinClass"),
            @Mapping(target = "stageJourney", constant = "RESERVATION"),
            @Mapping(source = "travelInfo.isInternational", target = "isInternational", defaultValue = "false")
    })
    PricingCalculateTravelInfo buildTravelInfo(PartnerReservationItem partnerReservationItem);


    default PartnerReservationItem getItemTypeFlight(PartnerReservationResponse partnerReservationResponse) {
        return partnerReservationResponse.getItems()
                .stream()
                .filter(item -> "FLIGHT".equals(item.getType()))
                .findFirst()
                .orElse(null);
    }

    default List<PricingCalculateItem> buildListPricingCalculateItems(PartnerReservationResponse partnerReservationResponse, PartnerReservationItem partnerReservationItemTypeFlight, String commerceOrderId) {
        var pricingCalculateItem = PricingCalculateItem.builder()
                .id(commerceOrderId)
                .flightType(partnerReservationItemTypeFlight.getTravelInfo().getType())
                .partnerCode(partnerReservationResponse.getPartnerCode())
                .price(toPricingCalculatePrice(partnerReservationResponse))
                .segments(buildSegments(partnerReservationItemTypeFlight))
                .build();
        return List.of(pricingCalculateItem);
    }


    @Mappings({
            @Mapping(source = "amount", target = "amount"),
            @Mapping(source = "ordersPriceDescription", target = "pricesDescription"),
            @Mapping(target = "flight.amount", expression = "java(getTotalFlight(partnerReservationResponse))"),
            @Mapping(target = "taxes.amount", expression = "java(getTotalTaxes(partnerReservationResponse))"),
            @Mapping(target = "currency", constant = "BRL")
    })
    PricingCalculatePrice toPricingCalculatePrice(PartnerReservationResponse partnerReservationResponse);

    default BigDecimal getTotalFlight(PartnerReservationResponse partnerReservationResponse) {
        return partnerReservationResponse.getItems().stream()
                .filter(item -> "FLIGHT".equals(item.getType()))
                .map(PartnerReservationItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal getTotalTaxes(PartnerReservationResponse partnerReservationResponse) {
        return partnerReservationResponse.getItems().stream()
                .filter(item -> "FLIGHT_TAX".equals(item.getType()))
                .map(PartnerReservationItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Mapping(target = "luggages", expression = "java(buildLuggages(partnerReservationSegment))")
    @Mapping(target = "cancellationRules", expression = "java(buildCancellationRules(partnerReservationSegment))")
    @Mapping(target = "changeRules", expression = "java(buildChangeRules(partnerReservationSegment))")
    PricingCalculateSegment toPricingCalculateSegment(PartnerReservationSegment partnerReservationSegment);

    default List<PricingCalculateSegment> buildSegments(PartnerReservationItem partnerReservationItemTypeFlight) {
        List<PricingCalculateSegment> segments = new ArrayList<>();
        for (PartnerReservationSegment segment : partnerReservationItemTypeFlight.getSegments()) {
            PricingCalculateSegment pricingCalculateSegment = toPricingCalculateSegment(segment);
            pricingCalculateSegment.setFlightsLegs(toPricingCalculateFlightsLegList(segment.getFlightLegs()));
            pricingCalculateSegment.setCabinClass(partnerReservationItemTypeFlight.getTravelInfo().getCabinClass());
            pricingCalculateSegment.setAirline(toPricingCalculateAirline(segment.getFlightLegs().getFirst().getAirline()));
            segments.add(pricingCalculateSegment);
        }
        return segments;
    }


    PricingCalculateLuggage toPricingCalculateLuggage(PartnerReservationLuggage partnerReservationLuggage);

    List<PricingCalculateLuggage> toPricingCalculateLuggageList(List<PartnerReservationLuggage> partnerReservationLuggages);

    default List<PricingCalculateLuggage> buildLuggages(PartnerReservationSegment segment) {
        return toPricingCalculateLuggageList(segment.getLuggages());
    }

    @Mapping(source = "description", target = "description")
    PricingCalculateCancellationRule toPricingCalculateCancellationRule(PartnerReservationCancellationRule partnerReservationCancellationRule);

    List<PricingCalculateCancellationRule> toPricingCalculateCancellationRuleList(List<PartnerReservationCancellationRule> partnerReservationCancelationRules);

    default List<PricingCalculateCancellationRule> buildCancellationRules(PartnerReservationSegment segment) {
        return toPricingCalculateCancellationRuleList(segment.getCancellationRules());
    }

    @Mapping(source = "description", target = "description")
    PricingCalculateChangeRule toPricingCalculateChangeRule(PartnerReservationChangeRule partnerReservationChangeRule);

    List<PricingCalculateChangeRule> toPricingCalculateChangeRuleList(List<PartnerReservationChangeRule> partnerReservationChangeRules);

    default List<PricingCalculateChangeRule> buildChangeRules(PartnerReservationSegment segment) {
        return toPricingCalculateChangeRuleList(segment.getChangeRules());
    }

    @Mapping(target = "iata", source = "managedBy.iata")
    @Mapping(target = "description", source = "managedBy.description")
    @Mapping(target = "managedBy", expression = "java(toPricingCalculateManagedBy(partnerReservationFlightLegAirline.getManagedBy()))")
    @Mapping(target = "operatedBy", expression = "java(toPricingCalculateOperatedBy(partnerReservationFlightLegAirline.getOperatedBy()))")
    PricingCalculateAirline toPricingCalculateAirline(PartnerReservationFlightLegAirline partnerReservationFlightLegAirline);

    PricingCalculateManagedBy toPricingCalculateManagedBy(PartnerReservationAirline managedBy);

    PricingCalculateOperatedBy toPricingCalculateOperatedBy(PartnerReservationAirline operatedBy);

    @Mapping(target = "airline", expression = "java(toPricingCalculateAirline(partnerReservationFlightLeg.getAirline()))")
    PricingCalculateFlightsLeg toPricingCalculateFlightsLeg(PartnerReservationFlightsLeg partnerReservationFlightLeg);

    default List<PricingCalculateFlightsLeg> toPricingCalculateFlightsLegList(List<PartnerReservationFlightsLeg> flightLegs) {
        return flightLegs.stream()
                .map(this::toPricingCalculateFlightsLeg)
                .toList();
    }

}

