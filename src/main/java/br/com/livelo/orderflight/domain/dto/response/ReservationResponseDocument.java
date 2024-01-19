package br.com.livelo.orderflight.domain.dto.response;

public record ReservationResponseDocument(String documentNumber,
										  String type,
										  String issueDate,
										  String issuingCountry,
										  String expirationDate,
										  String residenceCountry){

}
