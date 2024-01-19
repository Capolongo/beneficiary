package br.com.livelo.orderflight.domain.dto;

import br.com.livelo.orderflight.domain.dto.response.ReservationResponsePriceDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponsePriceDescriptionTest {

    @Test
    void buildObject() {
        var object = new ReservationResponsePriceDescription(BigDecimal.ONE, BigDecimal.ONE, "", "");
        assertNotNull(object);
    }
}
