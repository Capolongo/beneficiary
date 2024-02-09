package br.com.livelo.orderflight.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderProcessController {
  private final OrderService orderService;

  @GetMapping("/process")
  public ResponseEntity<List<OrderProcess>> getOrdersByStatus(@RequestParam String statusCode, @RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "${order.orderProcessMaxRows}") Integer rows) {
    log.debug("ConfirmationController.getOrdersByStatus() - Start - statusCode: [{}], page: [{}], rows: [{}]", statusCode, page, rows);
    var orders = orderService.getOrdersByStatusCode(statusCode, page, rows);

    log.debug("ConfirmationController.getOrdersByStatus() - End - response: [{}]", orders);
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(orders);
  }
}
