package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.headers.RequiredHeaders;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.service.confirmation.impl.ConfirmationServiceImpl;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class ConfirmationController {

    private final ConfirmationServiceImpl confirmationService;

    @PostMapping("{id}/confirmation")
    public ResponseEntity<ConfirmOrderResponse> confirmOrder(
        @PathVariable("id") String id, @RequestBody ConfirmOrderRequest order,
        @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, required = false) String transactionId,
        @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER, required = false) String userId) {

        RequiredHeaders requiredHeaders = new RequiredHeaders(transactionId, userId);

        log.info("ConfirmationController.confirmOrder() - Start - id: [{}], body: [{}]", id, order);
        var confirmOrderResponse = confirmationService.confirmOrder(id, order, requiredHeaders);

        log.info("ConfirmationController.confirmOrder() - End - response: [{}]", confirmOrderResponse);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(confirmOrderResponse);
    }
}
