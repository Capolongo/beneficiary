package br.com.livelo.br.com.livelo.services;

import br.com.livelo.br.com.livelo.dto.ProductDTO;
import br.com.livelo.br.com.livelo.services.impl.RabbitMQServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

class RabbitMQServiceTest {

    private final RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);
    private final RabbitMQServiceImpl rabbitMQService = new RabbitMQServiceImpl(rabbitTemplate);

    @Test
    void testSendMessage() {
        ReflectionTestUtils.setField(rabbitMQService, "enabled", true);
        ProductDTO productDTO = new ProductDTO();
        rabbitMQService.sendMessage(productDTO);
        verify(rabbitTemplate).convertAndSend(null, null, productDTO);
    }

    @Test
    void testOnMessage() {
        final Message message = Mockito.spy(new Message("messageTest".getBytes()));
        rabbitMQService.onMessage(message);
        verify(message).getBody();
    }

}
