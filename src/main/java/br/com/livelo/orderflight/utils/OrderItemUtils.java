package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.exception.OrderFlightException;
import lombok.experimental.UtilityClass;

import java.util.List;

import static br.com.livelo.orderflight.constants.AppConstants.TYPE_FLIGHT_TAX;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR;

@UtilityClass
public class OrderItemUtils {

    public static void hasMoreThanOneTravel(List<ReservationItem> items) {
        if (items.stream().filter(item -> !item.getProductType().toLowerCase().contains(TYPE_FLIGHT_TAX)).count() > 1 ||
                items.stream().filter(item -> item.getProductType().toLowerCase().contains(TYPE_FLIGHT_TAX)).count() > 1) {
            throw new OrderFlightException(ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR, null, "You can only add one travel per cart");
        }
    }
}
