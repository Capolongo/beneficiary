package br.com.livelo.orderflight.domain.dtos.payment.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentOptionDTO {
    private String id;
    private String name;
    private String description;
}
