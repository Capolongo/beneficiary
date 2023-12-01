package br.com.livelo.orderflight.services.impl;

import br.com.livelo.orderflight.services.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQServiceImpl implements RabbitMQService, MessageListener {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange:}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey:}")
    private String routingkey;

    @Value("${spring.rabbitmq.enabled}")
    private boolean enabled;

    public void sendMessage(ProductDTO product) {
        if (this.enabled)
            this.rabbitTemplate.convertAndSend(exchange, routingkey, product);
    }

    public void onMessage(Message message) {
        log.debug("Consuming Message - " + new String(message.getBody()));
    }
}
