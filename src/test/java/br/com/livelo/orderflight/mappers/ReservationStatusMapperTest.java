package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReservationStatusMapperTest {
    private ReservationStatusMapper mapper = new ReservationStatusMapperImpl();

    @Test
    void shouldBuildPartnerResposne() {
        var responseMock = mock(PartnerReservationResponse.class);
        assertDoesNotThrow(() -> this.mapper.buildPartnerResponse(responseMock, new OrderStatusEntity()));
    }
}
