package br.com.livelo.orderflight.service.installment;

import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;

public interface InstallmentService {
    InstallmentOptionsResponse getInstallmentOptions(String id, String paymentOptionId);
}
