package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.request.ItemRequestDTO;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import java.util.Set;

public class PayloadComparison {
    private PayloadComparison() {}
    public static boolean compareItems(Set<ItemRequestDTO> itemsOrder, Set<OrderItemEntity> itemsFoundOrder) {
        if (itemsOrder.size() != itemsFoundOrder.size()) {
            return false;
        }

        for (int i = 0; i < itemsOrder.size(); i++) {
            String commerceItemOrder = itemsOrder.stream().toList().get(i).getCommerceItemId();
            var ItemFoundOrder = itemsFoundOrder.stream().filter(
                    item -> item.getCommerceItemId().equals(commerceItemOrder)
            ).findFirst();

            if (ItemFoundOrder.isEmpty()) {
                return false;
            }

            if (!commerceItemOrder.equals(ItemFoundOrder.get().getCommerceItemId())) {
                return false;
            }
        }

        return true;
    }
}
