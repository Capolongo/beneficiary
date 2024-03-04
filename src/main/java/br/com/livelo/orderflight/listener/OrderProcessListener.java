package br.com.livelo.orderflight.listener;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
@Slf4j
public class OrderProcessListener {
    private final ObjectMapper objectMapper;
    private final ConfirmationService confirmationService;

    @RabbitListener(queues = "${spring.rabbitmq.order-flight-commands-getconfirmation.queue}", concurrency = "${spring.rabbitmq.order-flight-commands-getconfirmation.concurrency}")
    public void consumer(Message payload) {
        try {
            final OrderProcess orderProcess = objectMapper.readValue(MessageUtils.extractBodyAsString(payload), OrderProcess.class);
            confirmationService.orderProcess(orderProcess);
        } catch (Exception exception) {
            log.error("OrderProcessListener.consumer - error in processing payload [{}]", new String(payload.getBody(), StandardCharsets.UTF_8));
            log.error("OrderProcessListener.consumer - error", exception);
            throw new AmqpRejectAndDontRequeueException(exception);
        }
    }
}
