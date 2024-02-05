package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import java.util.Set;

public class PayloadComparison {
    public static boolean compareItems(Set<ConfirmOrderItemRequest> orderItemsRequest, Set<OrderItemEntity> orderItems) {
        if (orderItemsRequest.size() != orderItems.size()) {
            return false;
        }

        for (ConfirmOrderItemRequest orderItemRequest: orderItemsRequest) {
            String commerceItemOrder = orderItemRequest.getCommerceItemId();
            var ItemFoundOrder = orderItems.stream().filter(
                    item -> item.getCommerceItemId().equals(commerceItemOrder)).findFirst();

            if (ItemFoundOrder.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
