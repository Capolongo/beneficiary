package br.com.livelo.orderflight.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
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
  public ResponseEntity<PaginationOrderProcessResponse> getOrdersByStatus(@RequestParam String statusCode, @RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "${order.orderProcessMaxRows}") Integer rows) {
    log.debug("ConfirmationController.getOrdersByStatus() - Start - statusCode: [{}], page: [{}], rows: [{}]", statusCode, page, rows);
    var orders = orderService.getOrdersByStatusCode(statusCode, page, rows);

    log.debug("ConfirmationController.getOrdersByStatus() - End - response: [{}]", orders);
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(orders);
  }

}
