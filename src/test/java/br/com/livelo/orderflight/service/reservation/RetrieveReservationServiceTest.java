package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.config.PartnerProperties;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.orderflight.proxy.PartnerConnectorProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrieveReservationServiceTest {
    @InjectMocks
    private RetrieveReservationService retrieveReservationService;
    @Mock
    private PartnerConnectorProxy partnerConnectorProxy;

    @Mock
    private PartnerProperties partnerProperties;

    @Test
    void reservationUsingRetry_SuccessfulReservation() throws ReservationException {
        var requestMock = mock(PartnerReservationRequest.class);
        var responseMock = mock(PartnerReservationResponse.class);

        String transactionId = "123";
        Optional<Integer> attempt = Optional.of(3);
        when(partnerConnectorProxy.reservation(requestMock, transactionId)).thenReturn(responseMock);

        PartnerReservationResponse actualResponse = retrieveReservationService.reservationUsingRetry(requestMock, transactionId, attempt);

        assertEquals(responseMock, actualResponse);
        verify(partnerConnectorProxy, times(1)).reservation(requestMock, transactionId);
    }

    @Test
    void reservationUsingRetry_UnsuccessfulReservation() throws ReservationException {
        var requestMock = mock(PartnerReservationRequest.class);

        String transactionId = "123";
        Optional<Integer> attempt = Optional.of(3);

        when(partnerConnectorProxy.reservation(requestMock, transactionId)).thenThrow(
                new ReservationException(
                        ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR,
                        null,
                        "Erro interno ao se comunicar com parceiro no conector. ResponseBody: ",
                        null
                ));

        assertThrows(ReservationException.class, () -> retrieveReservationService.reservationUsingRetry(requestMock, transactionId, attempt));

        verify(partnerConnectorProxy, times(4)).reservation(requestMock, transactionId);
    }

    @Test
    void shouldReservationUsingRetryWithSuccess_WhenAttemptIsNullOrEmpty() throws ReservationException {
        var requestMock = mock(PartnerReservationRequest.class);
        var responseMock = mock(PartnerReservationResponse.class);

        String transactionId = "123";
        Optional<Integer> attempt = Optional.empty();

        when(partnerProperties.getAttemptByPartnerCode(requestMock.getPartnerCode())).thenReturn(null);

        when(partnerConnectorProxy.reservation(requestMock, transactionId)).thenReturn(responseMock);

        PartnerReservationResponse actualResponse = retrieveReservationService.reservationUsingRetry(requestMock, transactionId, attempt);

        assertEquals(responseMock, actualResponse);
    }
}