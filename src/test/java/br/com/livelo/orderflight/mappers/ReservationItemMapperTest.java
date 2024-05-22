package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationItemMapperTest {

    private final ReservationItemMapper mapper = new ReservationItemMapperImpl();

    @Test
    void shouldReturnEmptySegments() {
        var response = this.mapper.mapSegments(null);
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldReturnSegments() {
        var partnerReservationItem = PartnerReservationItem.builder()
                .segments(
                        List.of(PartnerReservationSegment.builder()
                                .luggages(List.of(PartnerReservationLuggage.builder().build()))
                                .cancellationRules(List.of(PartnerReservationCancellationRule.builder().build()))
                                .changeRules(List.of(PartnerReservationChangeRule.builder().build()))
                                .flightLegs(List.of(PartnerReservationFlightsLeg.builder().build()))
                                .build())
                )
                .build();
        var response = this.mapper.mapSegments(partnerReservationItem);
        assertFalse(response.isEmpty());
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
        when(partnerReservationItemMock.getType()).thenReturn("FLIGHT");
        var response = this.mapper.mapTravelInfo(requestMock, partnerReservationItemMock);
        assertNotNull(response);
    }
}
