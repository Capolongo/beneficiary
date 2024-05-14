package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.request.*;
import br.com.livelo.orderflight.exception.OrderFlightException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@UtilityClass
@Slf4j
public class PricingCalculateRequestMapper {
    private static final String FLIGHT = "FLIGHT";
    private static final String FLIGHT_TAX = "FLIGHT_TAX";
    private static final String RESERVATION = "RESERVATION";
    private static final String BRL = "BRL";

    public static PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse, String commerceOrderId) {
        var partnerReservationItemTypeFlight = getItemTypeFlight(partnerReservationResponse);

        return PricingCalculateRequest.builder()
                .travelInfo(buildTravelInfo(partnerReservationItemTypeFlight))
                .items(buildListPricingCalculateItems(partnerReservationResponse, partnerReservationItemTypeFlight, commerceOrderId))
                .build();
    }

    private static PricingCalculateTravelInfo buildTravelInfo(PartnerReservationItem partnerReservationItemTypeFlight) {
        Boolean isInternational = partnerReservationItemTypeFlight.getTravelInfo().getIsInternational() != null ? partnerReservationItemTypeFlight.getTravelInfo().getIsInternational() : false;
        return PricingCalculateTravelInfo.builder()
                .type(partnerReservationItemTypeFlight.getTravelInfo().getType())
                .adt(partnerReservationItemTypeFlight.getTravelInfo().getAdt())
                .chd(partnerReservationItemTypeFlight.getTravelInfo().getChd())
                .inf(partnerReservationItemTypeFlight.getTravelInfo().getInf())
                .cabinClass(partnerReservationItemTypeFlight.getTravelInfo().getCabinClass())
                .stageJourney(RESERVATION)
                .isInternational(isInternational)
                .build();
    }

    private static List<PricingCalculateItem> buildListPricingCalculateItems(PartnerReservationResponse partnerReservationResponse, PartnerReservationItem partnerReservationItemTypeFlight, String commerceOrderId) {
        var pricingCalculateItem = PricingCalculateItem.builder()
                .id(commerceOrderId)
                .flightType(partnerReservationItemTypeFlight.getTravelInfo().getType())
                .partnerCode(partnerReservationResponse.getPartnerCode())
                .price(buildPricingCalculatePrice(partnerReservationResponse))
                .segments(buildSegments(partnerReservationItemTypeFlight))
                .build();
        return List.of(pricingCalculateItem);
    }

    private static List<PricingCalculateSegment> buildSegments(PartnerReservationItem partnerReservationItemTypeFlight) {
        List<PricingCalculateSegment> segments = new ArrayList<>();
        for (PartnerReservationSegment segment : partnerReservationItemTypeFlight.getSegments()) {
            var flightsLegs = buildFlightsLegs(segment, partnerReservationItemTypeFlight);
            segments.add(PricingCalculateSegment.builder()
                    .step(Integer.valueOf(segment.getStep()))
                    .originIata(segment.getOriginIata())
                    .originCity(segment.getOriginCity())
                    .originAirport(segment.getOriginAirport())
                    .departureDate(segment.getDepartureDate())
                    .destinationIata(segment.getDestinationIata())
                    .destinationCity(segment.getDestinationCity())
                    .destinationAirport(segment.getDestinationAirport())
                    .arrivalDate(segment.getArrivalDate())
                    .stops(segment.getStops())
                    .flightDuration(segment.getFlightDuration())
                    .airline(flightsLegs.getFirst().getAirline())
                    .cabinClass(segment.getCabinClass())
                    .luggages(buildLuggages(segment))
                    .cancellationRules(buildCancellationRules(segment))
                    .changeRules(buildChangeRules(segment))
                    .flightsLegs(flightsLegs)
                    .build());
        }
        return segments;
    }

    private static List<PricingCalculateFlightsLeg> buildFlightsLegs(PartnerReservationSegment segment, PartnerReservationItem partnerReservationItemTypeFlight) {
        List<PricingCalculateFlightsLeg> flightsLegs = new ArrayList<>();
        for (PartnerReservationFlightsLeg partnerReservationFlightsLeg : segment.getFlightLegs()) {
            flightsLegs.add(PricingCalculateFlightsLeg.builder()
                    .airline(PricingCalculateAirline.builder()
                            .iata(partnerReservationFlightsLeg.getAirline().getManagedBy().getIata())
                            .description(partnerReservationFlightsLeg.getAirline().getManagedBy().getDescription())
                            .managedBy(PricingCalculateManagedBy.builder()
                                    .iata(partnerReservationFlightsLeg.getAirline().getManagedBy().getIata())
                                    .description(partnerReservationFlightsLeg.getAirline().getManagedBy().getDescription())
                                    .build())
                            .operatedBy(PricingCalculateOperatedBy.builder()
                                    .iata(partnerReservationFlightsLeg.getAirline().getOperatedBy().getIata())
                                    .description(partnerReservationFlightsLeg.getAirline().getOperatedBy().getDescription())
                                    .build())
                            .build())
                    .flightNumber(partnerReservationFlightsLeg.getFlightNumber())
                    .flightDuration(partnerReservationFlightsLeg.getFlightDuration())
                    .originIata(partnerReservationFlightsLeg.getOriginIata())
                    .timeToWait(partnerReservationFlightsLeg.getTimeToWait())
                    .originCity(partnerReservationFlightsLeg.getOriginCity())
                    .originAirport(partnerReservationFlightsLeg.getOriginAirport())
                    .departureDate(partnerReservationFlightsLeg.getDepartureDate())
                    .destinationIata(partnerReservationFlightsLeg.getDestinationIata())
                    .destinationCity(partnerReservationFlightsLeg.getDestinationCity())
                    .destinationAirport(partnerReservationFlightsLeg.getDestinationAirport())
                    .arrivalDate(partnerReservationFlightsLeg.getArrivalDate())
                    .cabinClass(partnerReservationItemTypeFlight.getTravelInfo().getCabinClass())
                    .build());
        }
        return flightsLegs;
    }

    private static List<PricingCalculateChangeRule> buildChangeRules(PartnerReservationSegment segment) {
        List<PricingCalculateChangeRule> changeRules = new ArrayList<>();
        for (PartnerReservationChangeRule partnerReservationChangeRule : segment.getChangeRules()) {
            changeRules.add(
                    PricingCalculateChangeRule.builder()
                            .description(partnerReservationChangeRule.getDescription())
                            .build()
            );
        }
        return changeRules;
    }

    private static List<PricingCalculateCancellationRule> buildCancellationRules(PartnerReservationSegment segment) {
        List<PricingCalculateCancellationRule> cancellationRules = new ArrayList<>();
        for (PartnerReservationCancellationRule partnerReservationCancellationRule : segment.getCancellationRules()) {
            cancellationRules.add(
                    PricingCalculateCancellationRule.builder()
                            .description(partnerReservationCancellationRule.getDescription())
                            .build()
            );
        }
        return cancellationRules;
    }

    private static List<PricingCalculateLuggage> buildLuggages(PartnerReservationSegment segment) {
        List<PricingCalculateLuggage> luggages = new ArrayList<>();

        for (PartnerReservationLuggage partnerReservationLuggage : segment.getLuggages()) {
            luggages.add(PricingCalculateLuggage.builder()
                    .type(partnerReservationLuggage.getType())
                    .quantity(partnerReservationLuggage.getQuantity())
                    .weight(partnerReservationLuggage.getWeight())
                    .measurement(partnerReservationLuggage.getMeasurement())
                    .description(partnerReservationLuggage.getDescription())
                    .build());
        }
        return luggages;
    }

    private static PricingCalculatePrice buildPricingCalculatePrice(PartnerReservationResponse partnerReservationResponse) {
        List<PricingCalculateTaxes> pricingCalculateTaxes = partnerReservationResponse.getOrdersPriceDescription().getTaxes().stream()
                .map(tax -> PricingCalculateTaxes.builder()
                        .type(tax.getType())
                        .amount(tax.getAmount())
                        .build())
                .collect(Collectors.toList());

        List<PricingCalculateFlight> pricingCalculateFlights = partnerReservationResponse.getOrdersPriceDescription().getFlights().stream()
                .map(flight -> PricingCalculateFlight.builder()
                        .passengerType(flight.getPassengerType())
                        .amount(flight.getAmount())
                        .passengerCount(flight.getPassengerCount())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalFlight = partnerReservationResponse.getItems().stream()
                .filter(item -> FLIGHT.equals(item.getType()))
                .map(PartnerReservationItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTaxes = partnerReservationResponse.getItems().stream()
                .filter(item -> FLIGHT_TAX.equals(item.getType()))
                .map(PartnerReservationItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PricingCalculatePrice.builder()
                .currency(BRL)
                .amount(partnerReservationResponse.getAmount())
                .pricesDescription(PricingCalculatePricesDescription.builder()
                        .flights(pricingCalculateFlights)
                        .taxes(pricingCalculateTaxes)
                        .build())
                .flight(PricingCalculateFlight.builder().amount(totalFlight).build())
                .taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
                .build();
    }

    private static PartnerReservationItem getItemTypeFlight(PartnerReservationResponse partnerReservationResponse) {
        return partnerReservationResponse.getItems()
                .stream()
                .filter(item -> FLIGHT.equals(item.getType()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));
    }
}
