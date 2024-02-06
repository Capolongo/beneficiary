package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationTravelInfoEntityMapperTest {
    private ReservationTravelInfoEntityMapper mapper = new ReservationTravelInfoEntityMapperImpl();

    @Test
    void shouldSetType() {
        var travelInfo = new TravelInfoEntity();
        var partnerReservationTravelInfo = PartnerReservationTravelInfo.builder().type("ROUND_TRIP").build();
        this.mapper.setType(partnerReservationTravelInfo, travelInfo);
        assertNotNull(travelInfo.getType());
    }
}
