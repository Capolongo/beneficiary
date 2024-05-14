package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;
import br.com.livelo.orderflight.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.livelo.orderflight.constants.DynatraceConstants.*;
import static br.com.livelo.orderflight.enuns.Flow.RESERVATION;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Slf4j
public class ReservationController {
    private final ReservationServiceImpl reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, required = false) String transactionId,
            @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER, required = false) String userId,
            @RequestHeader(value = "customerId", required = false) String customerId,
            @RequestHeader(value = "channel", required = false) String channel,
            @RequestHeader(value = "listPrice") String listPrice,
            @RequestBody ReservationRequest reservationRequest
    ) {
        MDC.put(COMMERCE_ORDER_ID, reservationRequest.getCommerceOrderId());
        MDC.put(TRANSACTION_ID, transactionId);
        MDC.put(FLOW, RESERVATION.name());
        MDC.put(PARTNER, reservationRequest.getPartnerCode());
        MDC.put(ERROR_TYPE, "SUCCESS");

        log.info("ReservationController.createReservation - Create reservation request: [{}]", LogUtils.writeAsJson(reservationRequest));
        var response = reservationService.createOrder(reservationRequest, transactionId, customerId, channel, listPrice, userId);
        log.info("ReservationController.createReservation - Create reservation response: [{}]", LogUtils.writeAsJson(response));
        MDC.clear();
        return ResponseEntity.ok(response);
    }

}
