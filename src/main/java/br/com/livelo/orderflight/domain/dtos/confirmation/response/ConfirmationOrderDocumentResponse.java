package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmationOrderDocumentResponse {
    private Long id;
    private String documentNumber;
    private String type;
    private String issueDate;
    private String issuingCountry;
    private String expirationDate;
    private String residenceCountry;
}
