package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

import java.util.List;

public class ConfirmOrderValidation {
    public static void validateOrderPayload(ConfirmOrderRequest orderRequest, OrderEntity order) throws Exception {
        List<Boolean> validationList = List.of(
                orderRequest.getCommerceOrderId().equals(order.getCommerceOrderId()),
                orderRequest.getPrice().getPointsAmount().equals(order.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(orderRequest.getItems(), order.getItems())
        );

        if (validationList.contains(false)) {
            throw new Exception("Objects are not equal");
        }


        if (!order.getCurrentStatus().getCode().equals("LIVPNR-1006") && !orderRequest.getResubmission()) {
            throw new Exception("Order is already confirmed");
        }

    }
}
