package br.com.livelo.orderflight.service.installment;

import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;

public interface InstallmentService {
    InstallmentOptionsResponse getInstallmentOptions(String id, String paymentOptionId);
}
