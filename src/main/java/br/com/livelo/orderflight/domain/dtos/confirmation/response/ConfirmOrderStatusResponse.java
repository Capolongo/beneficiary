package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmOrderStatusResponse {
    private String code;
    private String description;
    private String details;
}
