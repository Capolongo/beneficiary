package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;
import java.util.Set;

public record ReservationResponseSegment(String partnerId,
                                         String step,
                                         Integer stops,
                                         Integer flightDuration,
                                         String originIata,
                                         String originCity,
                                         String originAirport,
                                         String destinationIata,
                                         String destinationCity,
                                         String destinationAirport,
                                         LocalDateTime departureDate,
                                         LocalDateTime arrivalDate,
                                         Set<ReservationResponseLuggage> luggages,
                                         Set<ReservationResponseCancellationRule> cancellationRules,
                                         Set<ReservationResponseChangeRule> changeRules,
                                         Set<ReservationResponseFlightLeg> flightsLegs) {
}
