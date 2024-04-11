package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;

public record ReservationResponseFlightLeg(String flightNumber,
											Integer flightDuration,
											String airlineManagedByIata,
											String airlineManagedByDescription,
											String airlineOperatedByIata,
											String airlineOperatedByDescription,
											Integer timeToWait,
											String originIata,
											String originCity,
											String originAirport,
											String destinationIata,
											String destinationCity,
											String destinationAirport,
											LocalDateTime departureDate,
											LocalDateTime arrivalDate,
											String type) {

}
