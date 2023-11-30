package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.dto.ProductDTO;
import br.com.livelo.br.com.livelo.services.RabbitMQService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private RabbitMQService rabbitMQService;

    @InjectMocks
    private MessageController messageController;

    private static final String NAME = "Product one";
    private static final String DESCRIPTION = "Description one";
    private static final Double PRICE = 40.0;

    @Test
    void testSendMessage() {
        ResponseEntity<?> response = messageController.sendMessage(getProductDTO());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private ProductDTO getProductDTO() {
        return ProductDTO.builder()
                .name(NAME)
                .price(PRICE)
                .description(DESCRIPTION)
                .build();
    }

}
