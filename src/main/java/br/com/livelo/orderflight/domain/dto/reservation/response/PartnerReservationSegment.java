package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationSegment {
	private String partnerId;
    private String type;
    private String step;
    private Integer stops;
    private Integer flightDuration;
    private String originIata;
    private String originDescription;
    private String destinationIata;
    private String destinationDescription;
    private String departureDate;
    private String arrivalDate;
    private String cabinClass;
    private PartnerReservationAirline airline;
    private List<PartnerReservationFlightsLeg> flightLegs;
    private List<PartnerReservationLuggage> luggages;
    private List<PartnerReservationCancelationRule> cancelationRules;
    private List<PartnerReservationChangeRule> changeRules;
}
