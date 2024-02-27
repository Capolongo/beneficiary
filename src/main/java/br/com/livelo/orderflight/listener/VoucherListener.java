package br.com.livelo.orderflight.listener;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.MessageUtils;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class VoucherListener {
    private final ObjectMapper objectMapper;
    private final ConfirmationService confirmationService;

    @RabbitListener(queues = "${spring.rabbitmq.custom.order-flight-commands-getvoucher.queue}")
    public void consumer(Message payload) {
        try {
            final OrderProcess orderProcess = objectMapper.readValue(MessageUtils.extractBodyAsString(payload), OrderProcess.class);
            confirmationService.orderProcess(orderProcess);
        } catch (Exception exception) {
            throw new AmqpRejectAndDontRequeueException(exception);
        }
    }
}
