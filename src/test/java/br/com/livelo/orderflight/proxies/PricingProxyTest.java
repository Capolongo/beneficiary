package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PricingClient;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mock.MockBuilder;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PRICING_INTERNAL_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingProxyTest {

    @Mock
    private PricingClient pricingClient;

    @InjectMocks
    private PricingProxy proxy;

    @Test
    void shouldReturnPricingCalculate() {
        ResponseEntity<PricingCalculateResponse[]> pricingCalculateResponse = MockBuilder.pricingCalculateResponse();
        when(pricingClient.calculate(any(PricingCalculateRequest.class)))
                .thenReturn(pricingCalculateResponse);

        var response = proxy.calculate(mock(PricingCalculateRequest.class));

        assertNotNull(pricingCalculateResponse.getBody());
        assertEquals(Arrays.stream(pricingCalculateResponse.getBody()).toList(), response);
        assertEquals(200, pricingCalculateResponse.getStatusCode().value());
        verify(pricingClient).calculate(any(PricingCalculateRequest.class));
        verifyNoMoreInteractions(pricingClient);
    }

    @Test
    void shouldReturnFailedWhenCatchFeignException() throws OrderFlightException {
        FeignException mockException = Mockito.mock(FeignException.class);
        when(mockException.status()).thenReturn(500);
        when(pricingClient.calculate(
                any(PricingCalculateRequest.class)))
                .thenThrow(mockException);

        OrderFlightException exception = assertThrows(OrderFlightException.class, () -> proxy.calculate(mock(PricingCalculateRequest.class)));

        assertEquals(ORDER_FLIGHT_PRICING_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }


    @Test
    void shouldThrowException() {
        OrderFlightException exception = assertThrows(OrderFlightException.class, () -> proxy.calculate(mock(PricingCalculateRequest.class)));

        assertEquals(ORDER_FLIGHT_PRICING_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }
}
