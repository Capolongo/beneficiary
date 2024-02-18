package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PricingClient;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mock.MockBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingProxyTest {

  @Mock
  private PricingClient pricingClient;
  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private PricingProxy proxy;
  @Test
  void shouldReturnPricingCalculate() throws Exception {
    ResponseEntity<PricingCalculateResponse[]> pricingCalculateResponse = MockBuilder.pricingCalculateResponse();
    when(pricingClient.calculate(any(PricingCalculateRequest.class)))
        .thenReturn(pricingCalculateResponse);

    PricingCalculateResponse[] response = proxy.calculate(mock(PricingCalculateRequest.class));

    assertEquals(pricingCalculateResponse.getBody(), response);
    assertEquals(200, pricingCalculateResponse.getStatusCode().value());
    verify(pricingClient).calculate(any(PricingCalculateRequest.class));
    verifyNoMoreInteractions(pricingClient);
  }

  @Test
  void shouldReturnFailedWhenCatchOrderFlightException() throws OrderFlightException, JsonProcessingException {
    OrderFlightException mockException = Mockito.mock(OrderFlightException.class);
    when(pricingClient.calculate(
            any(PricingCalculateRequest.class)))
            .thenThrow(mockException);

    OrderFlightException exception = assertThrows(OrderFlightException.class, () -> {
      proxy.calculate(
              mock(PricingCalculateRequest.class));
    });

    assertTrue(Objects.nonNull(exception));
  }

  @Test
  void shouldReturnFailedWhenCatchFeignBusinessException() throws OrderFlightException, JsonProcessingException {
    FeignException mockException = Mockito.mock(FeignException.class);
    when(mockException.status()).thenReturn(400);
    when(pricingClient.calculate(
        any(PricingCalculateRequest.class)))
        .thenThrow(mockException);

    ConnectorReservationBusinessException exception = assertThrows(ConnectorReservationBusinessException.class, () -> {
      proxy.calculate(
              mock(PricingCalculateRequest.class));
    });

    assertTrue(exception.getArgs().contains("Business error on pricing calls"));
  }

  @Test
  void shouldReturnFailedWhenCatchFeignInternalException() throws OrderFlightException, JsonProcessingException {
    FeignException mockException = Mockito.mock(FeignException.class);
    when(mockException.status()).thenReturn(500);
    when(pricingClient.calculate(
            any(PricingCalculateRequest.class)))
            .thenThrow(mockException);

    ConnectorReservationInternalException exception = assertThrows(ConnectorReservationInternalException.class, () -> {
      proxy.calculate(
              mock(PricingCalculateRequest.class));
    });

    assertTrue(exception.getArgs().contains("Internal error on pricing calls"));
  }


  @Test
  void shouldThrowException() {
    Exception exception = assertThrows(Exception.class, () -> {
      proxy.calculate(
              mock(PricingCalculateRequest.class));
    });

    assertTrue(exception.getMessage().contains("Cannot invoke"));
  }
}
