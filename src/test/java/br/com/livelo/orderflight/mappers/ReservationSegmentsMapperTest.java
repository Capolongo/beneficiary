package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationSegmentsMapperTest {
    ReservationSegmentsMapper mapper = Mappers.getMapper(ReservationSegmentsMapper.class);

    @Test
    void shouldMapLuggages() {
        var segment = PartnerReservationSegment.builder()
                .luggages(List.of(PartnerReservationLuggage.builder().build()))
                .build();

        var response = this.mapper.mapLuggages(segment);
        assertNotNull(response);
    }

    @Test
    void shouldMapCancellationRules() {
        var segment = PartnerReservationSegment.builder()
                .cancelationRules(List.of(PartnerReservationCancelationRule.builder().build()))
                .build();

        var response = this.mapper.mapCancelationRules(segment);
        assertNotNull(response);
    }

    @Test
    void shouldMapChangeRules() {
        var segment = PartnerReservationSegment.builder()
                .changeRules(List.of(PartnerReservationChangeRule.builder().build()))
                .build();

        var response = this.mapper.mapChangeRules(segment);
        assertNotNull(response);
    }

    @Test
    void shouldMapFlightLegs() {
        var segment = PartnerReservationSegment.builder()
                .flightLegs(List.of(PartnerReservationFlightsLeg.builder().departureDate("2024-11-15T14:25:00.000-03:00").build()))
                .build();

        var response = this.mapper.mapFlightLeg(segment);
        assertNotNull(response);
    }
}
