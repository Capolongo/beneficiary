package br.com.livelo.orderflight.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationFlightsLeg {
	private String flightNumber;
    private Integer flightDuration;
    private String airline;
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
