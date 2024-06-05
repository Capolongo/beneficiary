package br.com.livelo.orderflight.domain.dto.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationDocument {
	private String number;
    private String type;
    private String issueDate;
    private String issuingCountry;
    private String expirationDate;
    private String residenceCountry;
}