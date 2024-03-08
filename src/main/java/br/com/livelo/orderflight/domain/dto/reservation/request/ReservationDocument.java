package br.com.livelo.orderflight.domain.dto.reservation.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationDocument {
    @NotBlank
    private String documentNumber;

    @NotBlank
	private String type;

    
    private String issueDate;


    private String issuingCountry;


    private String expirationDate;


    private String residenceCountry;
}
