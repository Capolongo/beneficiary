package br.com.livelo.orderflight.domain.dtos.connector.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerConfirmDocumentRequest {
    private String number;
    private String type;
    private String issueDate;
    private String issuingCountry;
    private String expirationDate;
    private String residenceCountry;
}
