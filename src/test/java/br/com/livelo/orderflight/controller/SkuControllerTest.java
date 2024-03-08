package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.service.shipment.ShipmentService;
import br.com.livelo.orderflight.service.sku.SkuService;
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
class SkuControllerTest {

  @Mock
  private SkuService service;

  @InjectMocks
  private SkuController controller;

  @Test
  void shouldReturnSuccessGetSku() {

    String id = anyString();
    String commerceItemId = anyString();
    String currency = anyString();

    when(service.getSku(id, commerceItemId, currency)).thenReturn(SkuItemResponse.builder().build());

    var response = controller.getSku(id, commerceItemId, currency);

    assertEquals(200, response.getStatusCode().value());
    verify(service).getSku(id, commerceItemId, currency);
  }
}
