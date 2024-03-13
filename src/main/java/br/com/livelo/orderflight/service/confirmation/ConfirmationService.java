package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.exception.OrderFlightException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ConfirmationService {
    ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws OrderFlightException;

    void orderProcess(OrderProcess payload);
}
