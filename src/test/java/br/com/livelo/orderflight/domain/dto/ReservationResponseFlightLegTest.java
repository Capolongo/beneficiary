package br.com.livelo.orderflight.domain.dto;

import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseAirlineDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseFlightLeg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponseFlightLegTest {

    @Test
    void buildObject() {
        var object = new ReservationResponseFlightLeg(
                "",
                1,
                "",
                "",
                "",
                "",
                "",
                1,
                "",
                "",
                "",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                ""
        );

        assertNotNull(object);
    }
}
