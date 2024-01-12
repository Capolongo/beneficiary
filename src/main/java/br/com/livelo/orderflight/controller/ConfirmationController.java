package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.service.confirmation.ConfirmationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class ConfirmationController {

    private final ConfirmationService confirmationService;

    @PostMapping("{id}/confirmation")
    public ResponseEntity<ConfirmOrderResponse> confirmOrder(@PathVariable("id") String id, @RequestBody ConfirmOrderRequest order)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(confirmationService.confirmOrder(id, order));
    }
}
