package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;
import java.util.Set;

public record ReservationResponseSegment(String partnerId,
                                         String step,
                                         Integer stops,
                                         Integer flightDuration,
                                         String originIata,
                                         String originDescription,
                                         String destinationIata,
                                         String destinationDescription,
                                         LocalDateTime departureDate,
                                         LocalDateTime arrivalDate,
                                         Set<ReservationResponseLuggage> luggages,
                                         Set<ReservationResponseCancelationRule> cancelationRules,
                                         Set<ReservationResponseChangeRule> changeRules,
                                         Set<ReservationResponseFlightLeg> flightsLegs) {

}
