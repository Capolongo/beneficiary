package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PricingClientTest {
    @Test
    void shouldCallPricingCalculate() {
        PricingCalculateRequest pricingCalculateRequest = mock(PricingCalculateRequest.class);

        List<PricingCalculateResponse> pricingCalculateResponse = new ArrayList<>();
        pricingCalculateResponse.add(mock(PricingCalculateResponse.class));
        ResponseEntity<List<PricingCalculateResponse>> responseEntity = ResponseEntity.ok(pricingCalculateResponse);

        URI baseUrl = URI.create("http://example.com");

        PricingClient pricingClient = mock(PricingClient.class);

        when(pricingClient.calculate(any(), anyString(), anyString())).thenReturn(responseEntity);

        ResponseEntity<List<PricingCalculateResponse>> result = pricingClient.calculate(pricingCalculateRequest, "123", "321");

        verify(pricingClient, times(1)).calculate(any(), anyString(), anyString());
        assertEquals(pricingCalculateResponse, result.getBody());
    }
}