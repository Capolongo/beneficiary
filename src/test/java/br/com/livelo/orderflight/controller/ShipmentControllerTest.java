package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.service.payment.PaymentService;
import br.com.livelo.orderflight.service.shipment.ShipmentService;
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
class ShipmentControllerTest {

  @Mock
  private ShipmentService service;

  @InjectMocks
  private ShipmentController controller;

  @Test
  void shouldReturnSuccessGetShipmentOptions() {

    String id = anyString();
    String postalCode = anyString();

    when(service.getShipmentOptions(id, postalCode)).thenReturn(ShipmentOptionsResponse.builder().build());

    var response = controller.getShipmentOptions(id, postalCode);

    assertEquals(200, response.getStatusCode().value());
    verify(service).getShipmentOptions(id, postalCode);
  }
}
