package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PricingCalculateSegment {
    private Integer step;
    private String originIata;
    private String originCity;
    private String originAirport;
    private String departureDate;
    private String destinationIata;
    private String destinationCity;
    private String destinationAirport;
    private String arrivalDate;
    private int stops;
    private int flightDuration;
    private PricingCalculateAirline airline;
    private String cabinClass;
    private List<PricingCalculateLuggage> luggages;
    private List<PricingCalculateCancellationRule> cancellationRules;
    private List<PricingCalculateChangeRule> changeRules;
    private List<PricingCalculateFlightsLeg> flightsLegs;
}
