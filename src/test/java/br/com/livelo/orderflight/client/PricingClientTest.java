package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PricingClientTest {
    @Test
    void shouldCallPricingCalculate() {
        PricingCalculateRequest pricingCalculateRequest = mock(PricingCalculateRequest.class);

        PricingCalculateResponse[] pricingCalculateResponse = new PricingCalculateResponse[]{mock(PricingCalculateResponse.class)};
        ResponseEntity<PricingCalculateResponse[]> responseEntity = ResponseEntity.ok(pricingCalculateResponse);

        URI baseUrl = URI.create("http://example.com");

        PricingClient pricingClient = mock(PricingClient.class);

        when(pricingClient.calculate(any())).thenReturn(responseEntity);

        ResponseEntity<PricingCalculateResponse[]> result = pricingClient.calculate(pricingCalculateRequest);

        verify(pricingClient, times(1)).calculate(any());
        assertEquals(pricingCalculateResponse, result.getBody());
    }
}