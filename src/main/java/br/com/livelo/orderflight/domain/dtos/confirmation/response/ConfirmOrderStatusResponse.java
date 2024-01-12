package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmOrderStatusResponse {
    public String code;
    public String description;
    public String details;
}
