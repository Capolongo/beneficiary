package br.com.livelo.orderflight.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.checkout.CheckoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("{id}/confirm")
    public ResponseEntity<OrderEntity> confirmOrder(@PathVariable("id") String id, @RequestBody OrderEntity order)
            throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(checkoutService.confirmOrder(id, order));
    }
}
