package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerReservationFlightsLeg {
	private String flightNumber;
    private Integer flightDuration;
    private PartnerReservationFlightLegAirline airline;
    private String managedBy;
    private String operatedBy;
    private String timeToWait;
    private String originIata;
    private String originDescription;
    private String destinationIata;
    private String destinationDescription;
    private String departureDate;
    private String arrivalDate;
    private String type;
}
