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
public class GetCompletedMQConfig {
    @Value("${spring.rabbitmq.order-flight-commands-getcompleted.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.order-flight-commands-getcompleted.queue}")
    private String queue ;

    @Value("${spring.rabbitmq.order-flight-commands-getcompleted.queueRoutingKey}")
    private String queueRoutingKey;

    @Bean("getCompletedExchange")
    public TopicExchange getCompletedExchange() {
        return new TopicExchange(exchange);
    }

    @Bean("getCompletedQueue")
    public Queue getCompletedQueue() {
        return new Queue(queue);
    }

    @Bean
    public Binding getCompletedBinding(@Qualifier("getCompletedQueue") Queue queue,
                                       @Qualifier("getCompletedExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueRoutingKey);
    }
}
