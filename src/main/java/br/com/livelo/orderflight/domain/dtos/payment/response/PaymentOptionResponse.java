package br.com.livelo.orderflight.domain.dtos.payment.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaymentOptionResponse {
    private List<PaymentOptionDTO> paymentOptions;
}
