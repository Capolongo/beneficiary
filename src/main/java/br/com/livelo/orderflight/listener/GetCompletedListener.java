package br.com.livelo.orderflight.listener;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.completed.CompletedService;
import br.com.livelo.orderflight.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetCompletedListener {
    private final ObjectMapper objectMapper;
    private final CompletedService completedService;


//    @RabbitListener(queues = "${spring.rabbitmq.order-flight-commands-getcompleted.queue}",
//            concurrency = "${spring.rabbitmq.order-flight-commands-getcompleted.concurrency}")
//    public void consumer(Message payload) {
//        try {
//            final OrderProcess orderProcess = objectMapper.readValue(MessageUtils.extractBodyAsString(payload), OrderProcess.class);
//            completedService.orderProcess(orderProcess);
//        } catch (Exception exception) {
//            throw new AmqpRejectAndDontRequeueException(exception);
//        }
//    }
}
