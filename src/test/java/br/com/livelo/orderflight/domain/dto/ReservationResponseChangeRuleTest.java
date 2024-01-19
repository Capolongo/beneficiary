package br.com.livelo.orderflight.domain.dto;

import br.com.livelo.orderflight.domain.dto.response.ReservationResponseChangeRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponseChangeRuleTest {

    @Test
    void buildObject() {
        var object = new ReservationResponseChangeRule("", "");

        assertNotNull(object);
    }
}
