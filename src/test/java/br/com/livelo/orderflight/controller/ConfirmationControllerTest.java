package br.com.livelo.orderflight.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;

@ExtendWith(MockitoExtension.class)
public class ConfirmationControllerTest {

  @InjectMocks
  private ConfirmationController controller;

  @Mock
  private ConfirmationService confirmationService;

  @Test
  void shouldReturnSuccessConfirmOrder() throws Exception {

    ConfirmOrderRequest requestBody = MockBuilder.confirmOrderRequest();
    ConfirmOrderResponse responseBody = MockBuilder.confirmOrderResponse();
    String id = "id";

    when(confirmationService.confirmOrder(anyString(), any(ConfirmOrderRequest.class))).thenReturn(responseBody);

    ResponseEntity<ConfirmOrderResponse> response = controller.confirmOrder(id, requestBody);

    assertEquals(responseBody, response.getBody());
    assertEquals(201, response.getStatusCode().value());
     verify(confirmationService).confirmOrder(id,
             requestBody);
     verifyNoMoreInteractions(confirmationService);
  }
}
