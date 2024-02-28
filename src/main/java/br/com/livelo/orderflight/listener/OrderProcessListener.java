package br.com.livelo.orderflight.listener;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderProcessListener {
    private final ObjectMapper objectMapper;
    private final ConfirmationService confirmationService;
    @RabbitListener(queues = "${spring.rabbitmq.custom.order-flight-commands-getconfirmation.queue}")
    public void consumer(Message payload) {
        try {
            final OrderProcess orderProcess = objectMapper.readValue(MessageUtils.extractBodyAsString(payload), OrderProcess.class);
            confirmationService.orderProcess(orderProcess);
        } catch (Exception exception) {
            throw new AmqpRejectAndDontRequeueException(exception);
        }
    }
}
