package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;

public interface ConfirmationService {
    ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws Exception;
}
