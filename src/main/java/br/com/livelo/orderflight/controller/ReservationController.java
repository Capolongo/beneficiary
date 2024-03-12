package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Slf4j
public class ReservationController {
    private final ReservationServiceImpl reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestHeader(value = "transactionId") String transactionId,
            @RequestHeader(value = "customerId", required = false) String customerId,
            @RequestHeader(value = "channel", required = false) String channel,
            @RequestHeader(value = "listPrice") String listPrice,
            @RequestBody @Valid ReservationRequest reservationRequest
    ) {
        MDC.put("transactionId", transactionId);
        MDC.put("flow", "RESERVATION");

        log.info("ReservationController.createReservation - Create reservation request: [{}]", reservationRequest);
        var response = reservationService.createOrder(reservationRequest, transactionId, customerId, channel, listPrice);
        log.info("ReservationController.createReservation - Create reservation response: [{}]", response);
        MDC.clear();
        return ResponseEntity.ok(response);
    }

}
