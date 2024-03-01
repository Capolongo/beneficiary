package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmationOrderPaxResponse {
    private String type;
    private String firstName;
    private String lastName;
    private String birthDate;
    private Set<ConfirmationOrderDocumentResponse> documents;
    private String email;
    private String areaCode;
    private String phone;
}
