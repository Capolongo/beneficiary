package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dto.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
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

        when(partnerConnectorClient.reservation(any(), any(), any())).thenReturn(responseEntity);

        ResponseEntity<PartnerReservationResponse> result = partnerConnectorClient.reservation(baseUrl, partnerReservationRequest, new LinkedMultiValueMap<>());

        verify(partnerConnectorClient, times(1)).reservation(any(), any(), any());
        assertEquals(partnerReservationResponse, result.getBody());
    }
}