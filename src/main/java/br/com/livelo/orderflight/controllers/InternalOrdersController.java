package br.com.livelo.orderflight.controllers;

import java.io.IOException;
import java.util.Optional;

import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.livelo.orderflight.entities.OrderEntity;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/orders")
public class InternalOrdersController {

    private final OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getById(@PathVariable String id) {
        final Optional<OrderEntity> orderOp = orderRepository.findById(id);

        return ResponseEntity.ok().body(orderOp.get());
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody @Valid OrderEntity orderEntity) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(orderRepository.save(orderEntity));
    }
}
