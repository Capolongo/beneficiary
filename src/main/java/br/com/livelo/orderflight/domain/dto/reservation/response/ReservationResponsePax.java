package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.util.Set;

public record ReservationResponsePax(String type,
									 String firstName,
									 String lastName,
									 String email,
									 String areaCode,
									 String phoneNumber,
									 String gender,
									 String birthDate,
									 Set<ReservationResponseDocument> documents) {

}
