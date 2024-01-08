package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart")
@Slf4j
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(
            @RequestHeader(value = "transactionId") String transacationId,
            @RequestHeader(value = "customerId", required = false) String customerId,
            @RequestHeader(value = "channel") String channel,
            @RequestBody CartRequest cartRequest
    ) {
        var response = cartService.createOrder(cartRequest);
        return ResponseEntity.ok(response);
    }
}
