package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

import java.util.List;

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
    private String originAirport;
    private String originCity;
    private String destinationIata;
    private String destinationAirport;
    private String destinationCity;
    private String departureDate;
    private String arrivalDate;
    private String cabinClass;
    private PartnerReservationAirline airline;
    private List<PartnerReservationFlightsLeg> flightLegs;
    private List<PartnerReservationLuggage> luggages;
    private List<PartnerReservationCancellationRule> cancellationRules;
    private List<PartnerReservationChangeRule> changeRules;
}
