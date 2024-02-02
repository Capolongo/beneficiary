package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;

import java.util.List;

public class ConfirmOrderValidation {
    public static void validateOrderPayload(ConfirmOrderRequest orderRequest, OrderEntity order)
            throws ReservationException {
        List<Boolean> validationList = List.of(
                orderRequest.getCommerceOrderId().equals(order.getCommerceOrderId()),
                orderRequest.getPrice().getPointsAmount().equals(order.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(orderRequest.getItems(), order.getItems()));

        if (validationList.contains(false)) {
            ReservationErrorType errorType = ReservationErrorType.VALIDATION_OBJECTS_NOT_EQUAL;
            throw new ReservationException(errorType,
                    errorType.getTitle(), null);
        }

        if (!order.getCurrentStatus().getCode().equals("LIVPNR-1006") && !orderRequest.getResubmission()) {
            ReservationErrorType errorType = ReservationErrorType.VALIDATION_ALREADY_CONFIRMED;
            throw new ReservationException(errorType,
                    errorType.getTitle(), null);
        }

    }
}
