package br.com.livelo.orderflight.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetVoucherMQConfig {
    @Value("${spring.rabbitmq.order-flight-commands-getvoucher.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.order-flight-commands-getvoucher.queue}")
    private String queue;

    @Value("${spring.rabbitmq.order-flight-commands-getvoucher.queueRoutingKey}")
    private String routingKey;

    @Bean("getVoucherExchange")
    public TopicExchange getVoucherExchange() {
        return new TopicExchange(exchange);
    }

    @Bean("getVoucherQueue")
    public Queue getVoucherQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding getVoucherBinding(@Qualifier("getVoucherQueue") Queue queue,
                                     @Qualifier("getVoucherExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
