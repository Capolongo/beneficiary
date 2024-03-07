package br.com.livelo.orderflight.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderProcessControllerTest {

  @Mock
  private OrderServiceImpl orderService;

  @InjectMocks
  private OrderProcessController controller;

  @Test
  void shouldReturnSuccessGetOrdersByStatusCode() throws Exception {

    String statusCode = "LIVPNR-1006";
    int page = 1;
    int rows = 4;

    PaginationOrderProcessResponse responseBody = MockBuilder.paginationOrderProcessResponse(page, rows);

    when(orderService.getOrdersByStatusCode(statusCode, page, rows)).thenReturn(responseBody);

    ResponseEntity<PaginationOrderProcessResponse> response = controller.getOrdersByStatus(statusCode, null, page,
        rows);

    assertEquals(responseBody, response.getBody());
    assertEquals(responseBody.getOrders().size(), response.getBody().getRows());
    assertEquals(200, response.getStatusCode().value());
    verify(orderService).getOrdersByStatusCode(statusCode, page, rows);
    verifyNoMoreInteractions(orderService);
  }

  @Test
  void shouldReturnSuccessGetOrdersByStatusCodeAndlimitExpirationDate() throws Exception {

    String statusCode = "LIVPNR-1006";
    int page = 1;
    int rows = 4;
    String limitExpirationDate = "2000-01-01";

    PaginationOrderProcessResponse responseBody = MockBuilder.paginationOrderProcessResponse(page, rows);

    when(orderService.getOrdersByStatusCodeAndlimitExpirationDate(statusCode, limitExpirationDate, page, rows))
        .thenReturn(responseBody);

    ResponseEntity<PaginationOrderProcessResponse> response = controller.getOrdersByStatus(statusCode,
        limitExpirationDate, page, rows);

    assertEquals(responseBody, response.getBody());
    assertEquals(responseBody.getOrders().size(), response.getBody().getRows());
    assertEquals(200, response.getStatusCode().value());
    verify(orderService).getOrdersByStatusCodeAndlimitExpirationDate(statusCode, limitExpirationDate, page, rows);
    verifyNoMoreInteractions(orderService);
  }

}
