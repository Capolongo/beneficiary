package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReservationMapperTest {
    private ReservationMapper mapper = new ReservationMapperImpl();
    @Test
    void shouldReturnDocument() {
        var documentMock = mock(ReservationDocument.class);
        var response = this.mapper.toPartnerReservationDocument(documentMock);
        assertNotNull(response);
    }

    @Test
    void shouldReturnNull() {
        var response = this.mapper.toPartnerReservationDocument(null);
        assertNull(response);
    }

    @Test
    void shouldReturnReservationResponseNull() {
        var response = this.mapper.toReservationResponse(null, 10);
        assertNull(response);
    }
}
