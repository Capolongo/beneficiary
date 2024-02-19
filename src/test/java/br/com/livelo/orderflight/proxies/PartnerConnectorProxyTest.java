package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ConnectorconnectorPartnersProxyTest {

    @InjectMocks
    private ConnectorPartnersProxy connectorPartnersProxy;

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
        when(partnerConnectorClient.createReserve(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(response));

        PartnerReservationResponse result = connectorPartnersProxy.createReserve(request, "transactionID");
        assertNotNull(result);
    }

    @Test
    void shouldThrowsException_WhenFeignResponse5xxStatus() {
        var request = mock(PartnerReservationRequest.class);

        var feignException = makeFeignMockExceptionWithStatus(500);
        makeException(request, feignException);

        var exception = assertThrows(ConnectorReservationInternalException.class,
                () -> connectorPartnersProxy.createReserve(request, "transactionId"));

        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldThrowsException_WhenFeignExceptionResponseIsDifferentOf5xxxStatus() {
        var request = mock(PartnerReservationRequest.class);
        var feignException = makeFeignMockExceptionWithStatus(400);

        makeException(request, feignException);

        var exception = assertThrows(ConnectorReservationBusinessException.class,
                () -> connectorPartnersProxy.createReserve(request, "transactionId"));

        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldThrowsOrderFlightException_WhenFeignExceptionResponseIsDifferentOf5xxxStatus() {
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);
        var request = mock(PartnerReservationRequest.class);
        when(request.getPartnerCode()).thenReturn("cvc");
        doThrow(OrderFlightException.class).when(partnerConnectorClient).createReserve(any(), any(), anyString());

        assertThrows(OrderFlightException.class, () -> connectorPartnersProxy.createReserve(request, "transactionId"));
    }

    @Test
    void shouldThrowException_WhenFeignReturnSomethingWrong() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();

        when(partnerConnectorClient.createReserve(any(), any(), any())).thenThrow(new RuntimeException("Simulated internal error"));
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        assertThrows(OrderFlightException.class,
                () -> connectorPartnersProxy.createReserve(mock(PartnerReservationRequest.class), "transactionId"));

        var exception = assertThrows(OrderFlightException.class,
                () -> connectorPartnersProxy.createReserve(request, "transactionId"));

        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldThrowFlightConnectorBusinessError_WhenThereIsBadRequest() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();
        var feignException = makeFeignMockExceptionWithStatus(400);
        makeException(request, feignException);

        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        var exception = assertThrows(ConnectorReservationBusinessException.class,
                () -> connectorPartnersProxy.createReserve(request, "transactionId"));

        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getOrderFlightErrorType());

    }

    @Test
    void shouldThrowFlightConnectorInternalError_WhenThereIsSomeInternalError() {
        var request = mock(PartnerReservationRequest.class);
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();
        var feignException = makeFeignMockExceptionWithStatus(400);
        makeException(request, feignException);
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);

        var exception = assertThrows(OrderFlightException.class,
                () -> connectorPartnersProxy.createReserve(request, "transactionId"));

        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getOrderFlightErrorType());

    }

    private void makeException(PartnerReservationRequest partnerReservationRequest, FeignException feignException) {
        var partnerWebhook = WebhookDTO.builder().connectorUrl("http://test").build();
        when(partnerReservationRequest.getPartnerCode()).thenReturn("cvc");
        when(partnersConfigService.getPartnerWebhook(anyString(), any())).thenReturn(partnerWebhook);
        when(partnerConnectorClient.createReserve(any(), any(), any())).thenThrow(feignException);
    }

    private FeignException makeFeignMockExceptionWithStatus(Integer statusCode) {
        var feignException = Mockito.mock(FeignException.class);
        Mockito.when(feignException.status()).thenReturn(statusCode);
        return feignException;
    }
}


