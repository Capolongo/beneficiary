package br.com.livelo.orderflight.domain.dto;

import br.com.livelo.orderflight.domain.dto.response.ReservationResponseSegment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationResponseSegmentTest {

    @Test
    void buildObject() {
        var object = new ReservationResponseSegment(
                "",
                "",
                1,
                1,
                "",
                "",
                "",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of()
        );

        assertNotNull(object);
    }
}
