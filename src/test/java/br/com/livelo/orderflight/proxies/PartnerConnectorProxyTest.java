package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.configs.PartnerProperties;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
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
    private ConnectorPartnersProxy connectorPartnersProxy;

    @Mock
    private PartnerConnectorClient partnerConnectorClient;

    @Mock
    private PartnerProperties partnerProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMakeReservation() {
        var request = mock(PartnerReservationRequest.class);
        var response = mock(PartnerReservationResponse.class);

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnerProperties.getUrlByPartnerCode(anyString())).thenReturn("http://teste.com");
        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.ok(response));

        PartnerReservationResponse result = connectorPartnersProxy.reservation(request, "transactionID");
        assertNotNull(result);
    }

    @Test
    void shouldThrowsException_WhenFeignResponse5xxStatus() {
        var request = mock(PartnerReservationRequest.class);

        var feignException = makeFeignMockExceptionWithStatus(500);
        makeException(request, feignException);

        var exception = assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowsException_WhenFeignExceptionResponseIsDifferentOf5xxxStatus() {
        var request = mock(PartnerReservationRequest.class);
        var feignException = makeFeignMockExceptionWithStatus(400);

        makeException(request, feignException);

        var exception = assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowException_WhenFeignReturnSomethingWrong() {
        var request = mock(PartnerReservationRequest.class);

        when(partnerConnectorClient.reservation(any(), any(), any())).thenThrow(new RuntimeException("Simulated internal error"));
        when(partnerProperties.getUrlByPartnerCode(anyString())).thenReturn("http://teste.com");

        assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(mock(PartnerReservationRequest.class), "transactionId"));

        var exception = assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldThrowFlightConnectorBusinessError_WhenThereIsBadRequest() {
        var request = mock(PartnerReservationRequest.class);

        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnerProperties.getUrlByPartnerCode(anyString())).thenReturn("http://teste.com");

        var exception =  assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, exception.getReservationErrorType());

    }

    @Test
    void shouldThrowFlightConnectorInternalError_WhenThereIsSomeInternalError() {
        var request = mock(PartnerReservationRequest.class);

        when(partnerConnectorClient.reservation(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        when(request.getPartnerCode()).thenReturn("cvc");
        when(partnerProperties.getUrlByPartnerCode(anyString())).thenReturn("http://teste.com");

        var exception =  assertThrows(ReservationException.class,
                () -> connectorPartnersProxy.reservation(request, "transactionId"));

        assertEquals(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, exception.getReservationErrorType());

    }

    private  void makeException(PartnerReservationRequest partnerReservationRequest, FeignException feignException) {
        when(partnerReservationRequest.getPartnerCode()).thenReturn("cvc");
        when(partnerProperties.getUrlByPartnerCode(anyString())).thenReturn("http://teste.com");
        when(partnerConnectorClient.reservation(any(), any(), any())).thenThrow(feignException);
    }

    private FeignException makeFeignMockExceptionWithStatus(Integer statusCode) {
        var feignException = Mockito.mock(FeignException.class);
        Mockito.when(feignException.status()).thenReturn(statusCode);

        return  feignException;
    }
}


