package br.com.livelo.orderflight.domain.dto.response;

import java.time.LocalDateTime;

public record ReservationResponseFlightLeg(String flightNumber,
											Integer flightDuration,
											String airline,
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
