package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;

public record ReservationResponseFlightLeg(String flightNumber,
											Integer flightDuration,
											String airlineManagedByIata,
											String airlineManagedByDescription,
											String airlineOperatedByIata,
											String airlineOperatedByDescription,
											String managedBy,
											Integer timeToWait,
											String originIata,
											String originDescription,
											String destinationIata,
											String destinationDescription,
											LocalDateTime departureDate,
											LocalDateTime arrivalDate,
											String type) {

}
