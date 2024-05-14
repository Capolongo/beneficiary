package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mappers.LiveloPartnersMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/orders")
public class InternalOrdersController {

    private final OrderRepository orderRepository;
    private final LiveloPartnersMapper liveloPartnersMapper;

    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getById(@PathVariable String id) {
        final Optional<OrderEntity> orderOp = orderRepository.findById(id);
        return ResponseEntity.ok().body(orderOp.orElse(null));
    }

    @PostMapping
    public ResponseEntity<OrderEntity> createOrder(@RequestBody @Valid OrderEntity orderEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
                .body(orderRepository.save(orderEntity));
    }

    @GetMapping("/mapper/{orderId}")
    public UpdateOrderDTO mapOrderToUpdateOrderDTO(@PathVariable String orderId) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        return liveloPartnersMapper.orderEntityToUpdateOrderDTO(order.get());
    }
}
