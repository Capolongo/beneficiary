package br.com.livelo.orderflight.domain.dtos.pricing.request;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

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
	private ArrayList<PricingCalculateLuggage> luggages;
	private ArrayList<PricingCalculateCancelationRule> cancelationRules;
	private ArrayList<PricingCalculateChangeRule> changeRules;
	private ArrayList<PricingCalculateFlightsLeg> flightsLegs;
}
