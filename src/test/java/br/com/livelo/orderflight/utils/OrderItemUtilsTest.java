package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.exception.OrderFlightException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderItemUtilsTest {
    @Test
    void shouldDoesNotThrowException_WhenItemsHasOneFlightItem() {
        var items = List.of(this.buildReservationItem("FLIGHT"));
        assertDoesNotThrow(() -> OrderItemUtils.hasMoreThanOneTravel(items));
    }

    @Test
    void shouldDoesNotThrowException_WhenItemsHasOneFlightTaxItem() {
        var items = List.of(this.buildReservationItem("TAX"));
        assertDoesNotThrow(() -> OrderItemUtils.hasMoreThanOneTravel(items));
    }

    @Test
    void shouldThrowException_WhenItemsHasMoreThanOneFlightItem() {
        var items = List.of(this.buildReservationItem("FLIGHT"), this.buildReservationItem("FLIGHT"));
        var exception = assertThrows(OrderFlightException.class, () -> OrderItemUtils.hasMoreThanOneTravel(items));
        assertEquals(ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldThrowException_WhenItemsHasMoreThanOneFlightTaxItem() {
        var items = List.of(this.buildReservationItem("TAX"), this.buildReservationItem("TAX"));
        var exception = assertThrows(OrderFlightException.class, () -> OrderItemUtils.hasMoreThanOneTravel(items));
        assertEquals(ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    private ReservationItem buildReservationItem(String productType) {
        return ReservationItem.builder()
                .productType(productType)
                .build();
    }
}
