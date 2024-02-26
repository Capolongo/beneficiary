package br.com.livelo.orderflight.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livelo.orderflight.service.confirmation.ConfirmationService;

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
    final String messageString = "{ \"id\": \"lf1213\", \"commerceOrderId\": \"o12313\" }";

    assertDoesNotThrow(() -> listener.consumer(new Message(messageString.getBytes())));

    verify(confirmationService, times(1)).orderProcess(any());
    verifyNoMoreInteractions(confirmationService);

  }

  // @Test
  // void throwJsonProcessingExceptionWhenMessageIsBroken() {
  // final String brokenMessage =
  // "{\"id\":\"trf123\"\"commerceOrderId:\"o12334\"}";

  // Assertions.assertThrows(JsonProcessingException.class,
  // () -> processGetOrderListener.consumeOrdersToGetFromPartner(brokenMessage));

  // verify(getOrderConnectorService,
  // times(0)).updateOrderByGetOnConnector(Mockito.any());

  // }

}
