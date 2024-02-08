package br.com.livelo.orderflight.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ReservationItemMapperTest {

    private ReservationItemMapper mapper = new ReservationItemMapperImpl();

    @Test
    void shouldReturnEmptySegments() {
        var response = this.mapper.mapSegments(null);
        assertTrue(response.isEmpty());
    }
}
