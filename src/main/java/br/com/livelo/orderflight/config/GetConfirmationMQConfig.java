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
public class GetConfirmationMQConfig {
    @Value("${spring.rabbitmq.order-flight-commands-getconfirmation.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.order-flight-commands-getconfirmation.queue}")
    private String queue ;

    @Value("${spring.rabbitmq.order-flight-commands-getconfirmation.queueRoutingKey}")
    private String queueRoutingKey;



    @Bean
    public TopicExchange getConfirmationExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue getConfirmationQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding getConfirmationBinding(@Qualifier("getConfirmationQueue") Queue queue,
                                          @Qualifier("getConfirmationExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueRoutingKey);
    }
}
