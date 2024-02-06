package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class PayloadComparison {
    public static boolean compareItems(Set<ConfirmOrderItemRequest> orderItemsRequest, Set<OrderItemEntity> orderItems) {
        if (orderItemsRequest.size() != orderItems.size()) {
            return false;
        }

        for (ConfirmOrderItemRequest orderItemRequest: orderItemsRequest) {
            String commerceItemOrder = orderItemRequest.getCommerceItemId();
            var itemFoundOrder = orderItems.stream().filter(
                    item -> item.getCommerceItemId().equals(commerceItemOrder)).findFirst();

            if (itemFoundOrder.isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
