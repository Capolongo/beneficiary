package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.status.StatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderStatusControllerTest {

  @Mock
  private StatusService service;

  @InjectMocks
  private OrderStatusController controller;

  @Test
  void shouldReturnSuccessUpdateStatus() {
    ConfirmOrderResponse responseBody = MockBuilder.confirmOrderResponse();

    when(service.updateStatus(anyString(), Mockito.any(UpdateStatusRequest.class))).thenReturn(responseBody);

    ResponseEntity<ConfirmOrderResponse> response = controller.updateStatusOrder("lf20", UpdateStatusRequest.builder().build());

    assertEquals(200, response.getStatusCode().value());
  }
}
