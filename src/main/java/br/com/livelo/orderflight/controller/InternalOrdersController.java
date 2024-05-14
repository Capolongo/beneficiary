package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mappers.LiveloPartnersMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/orders")
public class InternalOrdersController {

    private static final Logger log = LoggerFactory.getLogger(InternalOrdersController.class);
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

    @PutMapping("/log/{level}")
    public ResponseEntity<HttpStatus> testLog(@PathVariable String level) {
        var loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        var logLevel = Level.toLevel(level);
        loggerContext.getLogger("br.com.livelo.orderflight").setLevel(logLevel);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
