package br.com.livelo.orderflight.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessListener {
    @RabbitListener(queues = "${spring.rabbitmq.custom.order-flight-commands-getconfirmation.queue}")
    public void consumer(Message payload) {
        System.out.println("Message = " + payload.getBody());
    }
}
