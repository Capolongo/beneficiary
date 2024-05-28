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
        var validationList = List.of(
                orderRequest.getCommerceOrderId().equals(order.getCommerceOrderId()),
                orderRequest.getPrice().getPointsAmount().equals(order.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(orderRequest.getItems(), order.getItems()));

        if (validationList.contains(false)) {
            OrderFlightErrorType errorType = OrderFlightErrorType.ORDER_FLIGHT_CONFIRMATION_ORDER_VALIDATION_ERROR;
            throw new OrderFlightException(errorType, errorType.getTitle(), "Error on validate order to confirmation. id: " + order.getId());
        }

        if (!StatusLivelo.INITIAL.getCode().equals(order.getCurrentStatus().getCode()) && Boolean.TRUE.equals(!orderRequest.getResubmission())) {
            OrderFlightErrorType errorType = OrderFlightErrorType.ORDER_FLIGHT_CONFIRMATION_ALREADY_CONFIRMED_ERROR;
            throw new OrderFlightException(errorType, errorType.getTitle(), "Error on validate order to confirmation! Order already validated id: " + order.getId());
        }
    }
}
