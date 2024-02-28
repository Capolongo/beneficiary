package br.com.livelo.orderflight.service.voucher;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.exception.OrderFlightException;

public interface VoucherService {
    void orderProcess(OrderProcess payload) throws JsonProcessingException;
}