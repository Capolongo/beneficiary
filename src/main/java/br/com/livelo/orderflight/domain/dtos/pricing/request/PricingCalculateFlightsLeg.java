package br.com.livelo.orderflight.domain.dtos.pricing.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateFlightsLeg {
    private PricingCalculateAirline airline;
    private String flightNumber;
    private Integer flightDuration;
    private String originIata;
    private String timeToWait;
    private String originDescription;
    private String departureDate;
    private String destinationIata;
    private String destinationDescription;
    private String arrivalDate;
    @JsonProperty("class")
    private String flightClass;
}
