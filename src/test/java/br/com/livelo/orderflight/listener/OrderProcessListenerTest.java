package br.com.livelo.orderflight.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;

@ExtendWith(MockitoExtension.class)
class OrderProcessListenerTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private ConfirmationService confirmationService;

  @InjectMocks
  private OrderProcessListener listener;

  @Test
  void shouldProcessSuccessOrder() throws JsonProcessingException {
    final String messageString =
      "{ \"id\": \"lf1213\", \"commerceOrderId\": \"o12313\" }";

    assertDoesNotThrow(() ->
      listener.consumer(new Message(messageString.getBytes()))
    );

    verify(confirmationService, times(1)).orderProcess(any());
    verifyNoMoreInteractions(confirmationService);
  }

  @Test
  void shouldThrowAmqpRejectAndDontRequeueExceptionWhenMessageIsBroken()
    throws AmqpRejectAndDontRequeueException, JsonProcessingException, JsonMappingException {
    final String rawBody = "{ \"id\": \"lf1213\" }";

    when(objectMapper.readValue(rawBody, OrderProcess.class))
      .thenThrow(JsonProcessingException.class);

    Message brokenMessage = new Message(rawBody.getBytes());

    assertThrows(
      AmqpRejectAndDontRequeueException.class,
      () -> listener.consumer(brokenMessage)
    );

    verifyNoInteractions(confirmationService);
  }
}
