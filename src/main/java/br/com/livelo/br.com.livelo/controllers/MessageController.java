package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.dto.ProductDTO;
import br.com.livelo.br.com.livelo.services.RabbitMQService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/message")
public class MessageController {

    private final RabbitMQService rabbitMQService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid ProductDTO product) {
        rabbitMQService.sendMessage(product);
        return ResponseEntity.ok().build();
    }

}
