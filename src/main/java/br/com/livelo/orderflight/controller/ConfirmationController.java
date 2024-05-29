package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.headers.RequiredHeaders;
import br.com.livelo.orderflight.service.confirmation.impl.ConfirmationServiceImpl;
import br.com.livelo.orderflight.utils.DynatraceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.livelo.orderflight.constants.DynatraceConstants.*;
import static br.com.livelo.orderflight.enuns.Flow.CONFIRMATION;

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

        MDC.put(COMMERCE_ORDER_ID, order.getCommerceOrderId());
        MDC.put(TRANSACTION_ID, transactionId);
        MDC.put(FLOW, CONFIRMATION.name());
        MDC.put(PARTNER, order.getPartnerCode());
        RequiredHeaders requiredHeaders = new RequiredHeaders(transactionId, userId);

        log.info("ConfirmationController.confirmOrder() - Start - id: [{}], body: [{}]", id, order);
        var confirmOrderResponse = confirmationService.confirmOrder(id, order, requiredHeaders);

        DynatraceUtils.setDynatraceSuccessEntries();
        log.info("ConfirmationController.confirmOrder() - End - response: [{}]", confirmOrderResponse);
        MDC.clear();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(confirmOrderResponse);
    }
}
