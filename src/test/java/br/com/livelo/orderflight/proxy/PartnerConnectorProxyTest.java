package br.com.livelo.orderflight.proxy;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PartnerConnectorProxyTest {

    @InjectMocks
    private PartnerConnectorProxy partnerConnectorProxy;

    @Mock
    private PartnerConnectorClient partnerConnectorClient;

    @Mock
    private PartnersConfigService partnersConfigService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMakeReservation() {
        var request = mock(PartnerReservationRequest.class);
        var response = mock(PartnerReservationResponse.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);
        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(response));

        PartnerReservationResponse result = partnerConnectorProxy.reservation(request, "transactionID");
        assertNotNull(result);
    }

    @Test
    void shouldThrowsException_WhenFeignResponse5xxStatus() {
        var request = mock(PartnerReservationRequest.class);

        var feignException = makeFeignMockExceptionWithStatus(500);
        makeException(request, feignException);

        var exception = assertThrows(ConnectorReservationInternalException.class,
                () -> partnerConnectorProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowsException_WhenFeignExceptionResponseIsDifferentOf5xxxStatus() {
        var request = mock(PartnerReservationRequest.class);
        var feignException = makeFeignMockExceptionWithStatus(400);

        makeException(request, feignException);

        var exception = assertThrows(ConnectorReservationBusinessException.class,
                () -> partnerConnectorProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowException_WhenFeignReturnSomethingWrong() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();

        when(partnerConnectorClient.reservation(any(), any(), any())).thenThrow(new RuntimeException("Simulated internal error"));
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        assertThrows(ReservationException.class,
                () -> partnerConnectorProxy.reservation(mock(PartnerReservationRequest.class), "transactionId"));

        var exception = assertThrows(ReservationException.class,
                () -> partnerConnectorProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowFlightConnectorBusinessError_WhenThereIsBadRequest() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();

        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        var exception = assertThrows(ReservationException.class,
                () -> partnerConnectorProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getReservationErrorType());

    }

    @Test
    void shouldThrowFlightConnectorInternalError_WhenThereIsSomeInternalError() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();

        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        var exception = assertThrows(ReservationException.class,
                () -> partnerConnectorProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, exception.getReservationErrorType());

    }

    private void makeException(PartnerReservationRequest partnerReservationRequest, FeignException feignException) {
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();
        when(partnerReservationRequest.getPartnerCode()).thenReturn("cvc");
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);
        when(partnerConnectorClient.reservation(any(), any(), any())).thenThrow(feignException);
    }

    private FeignException makeFeignMockExceptionWithStatus(Integer statusCode) {
        var feignException = Mockito.mock(FeignException.class);
        Mockito.when(feignException.status()).thenReturn(statusCode);

        return feignException;
    }
}


