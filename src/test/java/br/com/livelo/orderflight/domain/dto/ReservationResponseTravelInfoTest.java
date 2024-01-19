package br.com.livelo.orderflight.domain.dto;

import br.com.livelo.orderflight.domain.dto.response.ReservationResponseTravelInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponseTravelInfoTest {

    @Test
    void buildReservationResponseTravelInfoTest() {
        var object = new ReservationResponseTravelInfo("", "", 1, 1, 1, "", "", new HashSet<>());

        assertNotNull(object);
    }
}
