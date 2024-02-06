package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PartnerConnectorClientTest {
    @Test
    void shouldCallPartnerReservation() {
        PartnerReservationRequest partnerReservationRequest = mock(PartnerReservationRequest.class);

        PartnerReservationResponse partnerReservationResponse = mock(PartnerReservationResponse.class);
        ResponseEntity<PartnerReservationResponse> responseEntity = ResponseEntity.ok(partnerReservationResponse);

        URI baseUrl = URI.create("http://example.com");

        PartnerConnectorClient partnerConnectorClient = mock(PartnerConnectorClient.class);

        when(partnerConnectorClient.createReserve(any(), any(), any())).thenReturn(responseEntity);

        ResponseEntity<PartnerReservationResponse> result = partnerConnectorClient.createReserve(baseUrl, partnerReservationRequest, "123");

        verify(partnerConnectorClient, times(1)).createReserve(any(), any(), any());
        assertEquals(partnerReservationResponse, result.getBody());
    }
}