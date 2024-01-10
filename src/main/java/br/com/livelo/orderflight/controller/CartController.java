package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart")
public class CartController {
    private final CartService cartService;

    //TODO PENSAR NOS LOGS QUE PODEM SER ADICIONADOS PARA CONSEGUIRMOS REALIZAR ANÁLISES EM PRD
    @PostMapping
    public ResponseEntity<CartResponse> createCart(
            @RequestHeader(value = "transactionId") String transacationId,
            @RequestHeader(value = "customerId", required = false) String customerId,
            @RequestHeader(value = "channel") String channel,
            @RequestBody @Valid CartRequest cartRequest
    ) {
        var response = cartService.createOrder(cartRequest, transacationId, customerId, channel);
        return ResponseEntity.ok(response);
    }
}
