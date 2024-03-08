package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.service.installment.InstallmentService;
import br.com.livelo.orderflight.service.installment.impl.InstallmentServiceImpl;
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
class InstallmentControllerTest {

  @Mock
  private InstallmentServiceImpl service;

  @InjectMocks
  private InstallmentController controller;

  @Test
  void shouldReturnSuccessGetInstallmentOptions() {

    String id = anyString();
    String paymentOptionId = anyString();

    when(service.getInstallmentOptions(id, paymentOptionId)).thenReturn(InstallmentOptionsResponse.builder().build());

    var response = controller.getInstallmentOptions(id, paymentOptionId);

    assertEquals(200, response.getStatusCode().value());
    verify(service).getInstallmentOptions(id, paymentOptionId);
  }
}
