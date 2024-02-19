package br.com.livelo.orderflight.listener;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderProcessListener {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;
    @RabbitListener(queues = "${spring.rabbitmq.custom.order-flight-commands-getconfirmation.queue}")
    public void consumer(Message payload) throws JsonProcessingException {
        var test = MessageUtils.extractBodyAsString(payload);
//        final OrderProcess orderProcess = objectMapper.readValue(test, OrderProcess.class);
        System.out.println(test);

//        orderService.orderProcess(orderProcess);
        System.out.println("payload = " + payload);
    }
}
