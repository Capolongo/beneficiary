package br.com.livelo.orderflight.service.validation;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.exception.OrderFlightException;

public interface OrderValidator {
    OrderValidateResponseDTO validateOrder(OrderValidateRequestDTO orderValidateRequest) throws OrderFlightException;
}
