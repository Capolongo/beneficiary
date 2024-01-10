package br.com.livelo.orderflight.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationSegment {
	private String partnerId;
    private String step;
    private Integer stops;
    private Integer flightDuration;
    private String originIata;
    private String originDescription;
    private String destinationIata;
    private String destinationDescription;
    private String departureDate;
    private String arrivalDate;
    private List<PartnerReservationFlightsLeg> flightsLegs;
    private List<PartnerReservationLuggage> luggages;
    private List<PartnerReservationCancelationRule> cancelationRules;
    private List<PartnerReservationChangeRule> changeRules;
}
