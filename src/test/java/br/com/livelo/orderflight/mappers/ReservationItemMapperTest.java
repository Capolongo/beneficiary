package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReservationItemMapperTest {

    private final ReservationItemMapper mapper = new ReservationItemMapperImpl();

    @Test
    void shouldReturnEmptySegments() {
        var response = this.mapper.mapSegments(null);
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldMapPrice() {
        var partnerReservationItemMock = mock(PartnerReservationItem.class);
        var response = this.mapper.mapPrice(partnerReservationItemMock, "");
        assertNotNull(response);
    }

    @Test
    void shouldMapTravelInfo() {
        var requestMock = mock(ReservationRequest.class);
        var partnerReservationItemMock = mock(PartnerReservationItem.class);
        var response = this.mapper.mapTravelInfo(requestMock, partnerReservationItemMock);
        assertNotNull(response);
    }
}
