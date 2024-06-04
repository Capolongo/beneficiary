package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Slf4j
public class OrderValidationController {

    private final OrderService orderService;


    @PostMapping("/validate")
    public OrderValidateResponseDTO validateOrder(@Valid @RequestBody(required = true) final OrderValidateRequestDTO orderValidateRequest) {
        log.debug("OrderValidationController.validateOrder() - Start - Partner retrieve with request: {}", orderValidateRequest);
        OrderValidateResponseDTO orderValidateResponse = orderService.validateOrderList(orderValidateRequest);
        log.debug("OrderValidationController.validateOrder() - End - Partner retrieve with response: {}", orderValidateResponse);
        return orderValidateResponse;
    }
}
