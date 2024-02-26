package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.request.*;
import br.com.livelo.orderflight.exception.OrderFlightException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;


@UtilityClass
public class PricingCalculateRequestMapper {
    private static final String TYPE_FLIGHT = "type_flight";
    private static final String RESERVATION = "RESERVATION";
    private static final String BRL = "BRL";

    public static PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse) {
        var partnerReservationItemTypeFlight = getItemTypeFlight(partnerReservationResponse);
        return PricingCalculateRequest.builder()
                .travelInfo(buildTravelInfo(partnerReservationItemTypeFlight))
                .items(buildListPricingCalculateItens(partnerReservationResponse, partnerReservationItemTypeFlight))
                .build();
    }

    private static PricingCalculateTravelInfo buildTravelInfo(PartnerReservationItem partnerReservationItemTypeFlight) {
        return PricingCalculateTravelInfo.builder()
                .type(partnerReservationItemTypeFlight.getTravelInfo().getType())
                .adultQuantity(partnerReservationItemTypeFlight.getTravelInfo().getAdultQuantity())
                .childQuantity(partnerReservationItemTypeFlight.getTravelInfo().getChildQuantity())
                .babyQuantity(partnerReservationItemTypeFlight.getTravelInfo().getBabyQuantity())
                .typeClass(partnerReservationItemTypeFlight.getTravelInfo().getTypeClass())
                .stageJourney(RESERVATION)
                .build();
    }

    private static List<PricingCalculateItem> buildListPricingCalculateItens(PartnerReservationResponse partnerReservationResponse, PartnerReservationItem partnerReservationItemTypeFlight) {
        var pricingCalculateItem = PricingCalculateItem.builder()
                .id(partnerReservationResponse.getCommerceOrderId())
                .flightType(partnerReservationItemTypeFlight.getTravelInfo().getType())
                .price(buildPricingCalculatePrice(partnerReservationResponse, partnerReservationResponse))
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
                    .originDescription(segment.getOriginDescription())
                    .originDate(segment.getDepartureDate())
                    .destinationIata(segment.getDestinationIata())
                    .destinationDescription(segment.getDestinationDescription())
                    .destinationDate(segment.getArrivalDate())
                    .numberOfStops(segment.getStops())
                    .flightDuration(segment.getFlightDuration())
                    .airline(flightsLegs.getFirst().getAirline())
                    .flightClass(null)//ACHAR
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
                                    .iata(partnerReservationFlightsLeg.getManagedBy())
                                    .description(partnerReservationFlightsLeg.getManagedBy())
                                    .build())
                            .operatedBy(PricingCalculateOperatedBy.builder()
                                    .iata(partnerReservationFlightsLeg.getOperatedBy())
                                    .description(partnerReservationFlightsLeg.getOperatedBy())
                                    .build())
                            .build())
                    .flightNumber(partnerReservationFlightsLeg.getFlightNumber())
                    .flightDuration(partnerReservationFlightsLeg.getFlightDuration())
                    .originIata(partnerReservationFlightsLeg.getOriginIata())
                    .timeToWait(partnerReservationFlightsLeg.getTimeToWait())
                    .originDescription(partnerReservationFlightsLeg.getOriginDescription())
                    .departureDate(partnerReservationFlightsLeg.getDepartureDate())
                    .destinationIata(partnerReservationFlightsLeg.getDestinationIata())
                    .destinationDescription(partnerReservationFlightsLeg.getDestinationDescription())
                    .arrivalDate(partnerReservationFlightsLeg.getArrivalDate())
                    .flightClass(partnerReservationItemTypeFlight.getTravelInfo().getTypeClass())
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
        for (PartnerReservationCancelationRule partnerReservationCancelationRule : segment.getCancelationRules()) {
            cancellationRules.add(
                    PricingCalculateCancellationRule.builder()
                            .description(partnerReservationCancelationRule.getDescription())
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
                    .quantity(null)//ACHAR
                    .weight(null)//ACHAR
                    .measurement(null)//ACHAR
                    .description(partnerReservationLuggage.getDescription())
                    .build());
        }
        return luggages;
    }

    private static PricingCalculatePrice buildPricingCalculatePrice(PartnerReservationResponse response, PartnerReservationResponse partnerReservationResponse) {
        var totalTaxes = BigDecimal.ZERO;
        var totalFlight = BigDecimal.ZERO;

        List<PricingCalculateTaxes> pricingCalculateTaxes = new ArrayList<>();
        List<PricingCalculateFlight> pricingCalculateFlights = new ArrayList<>();
        for(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes : partnerReservationResponse.getOrdersPriceDescription().getTaxes()) {
            totalTaxes = totalTaxes.add(partnerReservationOrdersPriceDescriptionTaxes.getAmount());
            pricingCalculateTaxes.add(PricingCalculateTaxes.builder()
                    .type(partnerReservationOrdersPriceDescriptionTaxes.getDescription())
                    .amount(partnerReservationOrdersPriceDescriptionTaxes.getAmount())
                    .build());
        }

        for(PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight : partnerReservationResponse.getOrdersPriceDescription().getFlights()) {
            totalFlight = totalFlight.add(partnerReservationOrdersPriceDescriptionFlight.getAmount());
            pricingCalculateFlights.add(PricingCalculateFlight.builder()
                    .passengerType(partnerReservationOrdersPriceDescriptionFlight.getPassengerType())
                    .amount(partnerReservationOrdersPriceDescriptionFlight.getAmount())
                    .passengerCount(1)
                    .build());
        }

        var priceDescription = PricingCalculatePricesDescription.builder()
                .flights(pricingCalculateFlights)
                .taxes(pricingCalculateTaxes)
                .build();

        return PricingCalculatePrice.builder()
                .currency(BRL)
                .amount(partnerReservationResponse.getAmount())
                .pricesDescription(priceDescription)
                .flight(PricingCalculateFlight.builder().amount(totalFlight).build())
                .taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
                .build();
    }

    private static PartnerReservationItem getItemTypeFlight(PartnerReservationResponse partnerReservationResponse) {
        return partnerReservationResponse.getItems()
                .stream()
                .filter(item -> TYPE_FLIGHT.equals(item.getType()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));
    }
}
