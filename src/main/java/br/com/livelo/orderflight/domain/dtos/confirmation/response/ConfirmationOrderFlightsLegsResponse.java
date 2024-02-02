package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConfirmationOrderFlightsLegsResponse {
    private String flightNumber;
    private int flightDuration;
    private String airline;
    private String managedBy;
    private String operatedBy;
    private String timeToWait;
    private String originIata;
    private String originDescription;
    private String destinationIata;
    private String destinationDescription;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String type;
}
