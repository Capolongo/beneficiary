package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.ReservationResponse;
import br.com.livelo.orderflight.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class ReservationController {
    private final ReservationService reservationService;

    //TODO PENSAR NOS LOGS QUE PODEM SER ADICIONADOS PARA CONSEGUIRMOS REALIZAR ANÁLISES EM PRD
    @PostMapping
    public ResponseEntity<ReservationResponse> createCart(
            @RequestHeader(value = "transactionId") String transacationId,
            @RequestHeader(value = "customerId", required = false) String customerId,
            @RequestHeader(value = "channel") String channel,
            @RequestHeader(value = "listPrice") String listPrice,
            @RequestBody @Valid ReservationRequest reservationRequest
    ) {
        var response = reservationService.createOrder(reservationRequest, transacationId, customerId, channel, listPrice);
        return ResponseEntity.ok(response);
    }
}
