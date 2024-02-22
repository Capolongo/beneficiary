package br.com.livelo.orderflight.domain.dtos.pricing.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PricingCalculateSegment {
    private String type;
    private Integer step;
    private String originIata;
    private String originDescription;
    private String originDate;
    private String destinationIata;
    private String destinationDescription;
    private String destinationDate;
    private int numberOfStops;
    private int flightDuration;
    private PricingCalculateAirline airline;
    @JsonProperty("class")
    private String flightClass;
    private List<PricingCalculateLuggage> luggages;
    private List<PricingCalculateCancellationRule> cancellationRules;
    private List<PricingCalculateChangeRule> changeRules;
    private List<PricingCalculateFlightsLeg> flightsLegs;
}
