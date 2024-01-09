package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import java.util.Set;

public class PayloadComparison {
    private PayloadComparison() {}
    public static boolean compareItems(Set<OrderItemEntity> itemsOrder, Set<OrderItemEntity> itemsFoundOrder) {
        if (itemsOrder.size() != itemsFoundOrder.size()) {
            return false;
        }

        for (int i = 0; i < itemsOrder.size(); i++) {
            String commerceItemOrder = itemsOrder.stream().toList().get(i).getCommerceItemId();
            String commerceItemFoundOrder = itemsFoundOrder.stream().toList().get(i).getCommerceItemId();

            if (!commerceItemOrder.equals(commerceItemFoundOrder)) {
                return false;
            }
        }

        return true;
    }
}
