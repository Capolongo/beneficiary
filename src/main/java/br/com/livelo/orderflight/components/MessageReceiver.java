package br.com.livelo.orderflight.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "enabled", havingValue = "true")
public class MessageReceiver {

    @RabbitListener(id = "product-listener", queues = "product.queue")
    public void receiveMessage(ProductDTO product) {
        log.info("Received message, product name: " + product.getName());
    }

}
