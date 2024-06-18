package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponsePrice;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ReservationControllerTest {
    @InjectMocks
    private ReservationController reservationController;
    @Mock
    private ReservationServiceImpl reservationService;

    @Test
    void shouldCreateReservation() {
        var expected = mock(ReservationResponse.class);
        var request = mock(ReservationRequest.class);
        var priceMock = mock(ReservationResponsePrice.class);
        Mockito.when(reservationService.createOrder(any(ReservationRequest.class), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(expected);
        when(expected.price()).thenReturn(priceMock);

        var response = this.reservationController.createReservation("123", "123", "123", "WEB", "price", request);
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertEquals(expected, response.getBody())
        );
    }
}
