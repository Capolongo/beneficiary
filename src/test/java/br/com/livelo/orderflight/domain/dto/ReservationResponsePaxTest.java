package br.com.livelo.orderflight.domain.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponsePaxTest {

    @Test
    void buildObject() {
        var object = new ReservationResponsePax("", "", "", "", "", "", "", "", Set.of());
        assertNotNull(object);
    }
}
