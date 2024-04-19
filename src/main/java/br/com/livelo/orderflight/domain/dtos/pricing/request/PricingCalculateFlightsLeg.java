package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateFlightsLeg {
    private PricingCalculateAirline airline;
    private String flightNumber;
    private Integer flightDuration;
    private String originIata;
    private String originCity;
    private String originAirport;
    private String timeToWait;
    private String departureDate;
    private String destinationIata;
    private String destinationCity;
    private String destinationAirport;
    private String arrivalDate;
    private String cabinClass;
    private String fareBasis;
    private String fareClass;
}
