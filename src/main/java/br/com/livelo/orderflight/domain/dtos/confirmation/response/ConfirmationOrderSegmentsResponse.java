package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ConfirmationOrderSegmentsResponse {
    private String partnerId;
    private String step;
    private int stops;
    private int flightDuration;
    private String originIata;
//    private String originDescription;
    private String destinationIata;
//    private String destinationDescription;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private Set<ConfirmationOrderFlightsLegsResponse> flightsLegs;
    private Set<ConfirmationOrderLuggagesResponse> luggages;
    private Set<ConfirmationOrderCancaletionRulesResponse> cancelationRules;
    private Set<ConfirmationOrderChangeRulesResponse> changeRules;
}