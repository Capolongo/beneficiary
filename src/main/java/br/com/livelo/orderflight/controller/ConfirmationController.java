package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.repository.FindAllOrdersByStatusCode;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.service.confirmation.impl.ConfirmationServiceImpl;
import br.com.livelo.orderflight.service.order.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class ConfirmationController {

    private final ConfirmationServiceImpl confirmationService;
    private final OrderService orderService;

    @PostMapping("{id}/confirmation")
    public ResponseEntity<ConfirmOrderResponse> confirmOrder(@PathVariable("id") String id, @RequestBody ConfirmOrderRequest order) {
        log.info("ConfirmationController.confirmOrder() - Start - id: [{}], body: [{}]", id, order);
        var confirmOrderResponse = confirmationService.confirmOrder(id, order);

        log.info("ConfirmationController.confirmOrder() - End - response: [{}]", confirmOrderResponse);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(confirmOrderResponse);
    }

    @GetMapping
    public ResponseEntity<List<FindAllOrdersByStatusCode>> getOrdersByStatus(@RequestParam String statusCode, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "${order.findAllOrdersByStatusMaxLimit}") Integer limit) {
        log.info("ConfirmationController.getOrdersByStatus() - Start - statusCode: [{}], page: [{}], limit: [{}]", statusCode, page, limit);
        var orders = orderService.getOrdersByStatusCode(statusCode, page, limit);

        log.info("ConfirmationController.getOrdersByStatus() - End - response: [{}]", orders);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(orders);
    }

}
