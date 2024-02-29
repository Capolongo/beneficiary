package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.service.payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

  @Mock
  private PaymentService paymentService;

  @InjectMocks
  private PaymentController controller;

  @Test
  void shouldReturnSuccessGetPaymentOptions() {

    String id = anyString();
    String shipmentOptionId = anyString();

    when(paymentService.getPaymentOptions(id, shipmentOptionId)).thenReturn(PaymentOptionResponse.builder().build());

    var response = controller.getPaymentOptions(id, shipmentOptionId);

    assertEquals(200, response.getStatusCode().value());
    verify(paymentService).getPaymentOptions(id, shipmentOptionId);
  }
}
