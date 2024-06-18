package br.com.livelo.orderflight.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@ExtendWith(MockitoExtension.class)
class GetCompletedMQConfigTest {

    @Test
    void shouldProcessSuccessOrder() {
        GetCompletedMQConfig getCompletedMQConfig = new GetCompletedMQConfig();
        ReflectionTestUtils.setField(getCompletedMQConfig, "queue", "queue");
        TopicExchange getCompletedExchange = getCompletedMQConfig.getCompletedExchange();
        Queue queue = getCompletedMQConfig.getCompletedQueue();
        Binding config = getCompletedMQConfig.getCompletedBinding(queue, getCompletedExchange);

        assertInstanceOf(Binding.class, config);
    }
}
