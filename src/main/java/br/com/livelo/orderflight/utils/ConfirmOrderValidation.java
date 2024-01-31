package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;

import java.util.List;

public class ConfirmOrderValidation {
    public static void validateRequestPayload(ConfirmOrderRequest order, OrderEntity foundOrder) throws Exception {
        List<Boolean> validationList = List.of(
                order.getCommerceOrderId().equals(foundOrder.getCommerceOrderId()),
                order.getPrice().getPointsAmount().equals(foundOrder.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(order.getItems(), foundOrder.getItems())
        );

        if (validationList.contains(false)) {
            throw new Exception("Objects are not equal");
        }
    }

    public static void validateIfAlreadyIsConfirmed(OrderStatusEntity orderStatus, Boolean resubmission) throws Exception {
        if (orderStatus.getCode().equals("LIVPNR-1006") || resubmission) {
            return;
        }

        throw new Exception("Order is already confirmed");
    }

    public static void validatePartnerOrderIds(String foundOrderId, String partnerConnectorOrderId) throws Exception {
        if (!partnerConnectorOrderId.equals(foundOrderId)) {
            throw new Exception("PartnerOrderIds are not equal");
        }
    }
}
