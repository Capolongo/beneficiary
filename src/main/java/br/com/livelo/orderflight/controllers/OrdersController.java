package br.com.livelo.orderflight.controllers;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getFindAll() {

        return ResponseEntity.ok().body(orderService.findAll());
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createCampaign(@RequestBody @Valid OrderEntity orderEntity) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(orderService.save(orderEntity));
    }
}
