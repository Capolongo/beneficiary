package br.com.livelo.orderflight.service.payment;

import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;

public interface PaymentService {
    PaymentOptionResponse getPaymentOptions(String id, String shipmentOptionId);

}
