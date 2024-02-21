package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ConfirmOrderValidation {
    public static void validateOrderPayload(ConfirmOrderRequest orderRequest, OrderEntity order) throws OrderFlightException {
        List<Boolean> validationList = List.of(
                orderRequest.getCommerceOrderId().equals(order.getCommerceOrderId()),
                orderRequest.getPrice().getPointsAmount().equals(order.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(orderRequest.getItems(), order.getItems()));

        if (validationList.contains(false)) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_OBJECTS_NOT_EQUAL;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        if (!StatusLivelo.INITIAL.getCode().equals(order.getCurrentStatus().getCode()) && !orderRequest.getResubmission().booleanValue()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_ALREADY_CONFIRMED;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }
}
