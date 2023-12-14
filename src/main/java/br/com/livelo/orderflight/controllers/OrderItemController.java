package br.com.livelo.orderflight.controllers;

import br.com.livelo.orderflight.entities.OrderItemEntity;
import br.com.livelo.orderflight.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/orderItem/internal")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemEntity>> getFindAll() {

        return ResponseEntity.ok().body(orderItemService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> createCampaign(@RequestBody @Valid OrderItemEntity orderItemEntity) throws IOException {
        orderItemService.save(orderItemEntity);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body("ok");
    }
}
