package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

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
        Mockito.when(reservationService.createOrder(any(ReservationRequest.class), anyString(), anyString(), anyString(), anyString())).thenReturn(expected);

        var response = this.reservationController.createReservation("123", "123", "WEB", "price", request);
        assertAll(
                () -> assertFalse(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertEquals(expected, response.getBody())
        );
    }
}
