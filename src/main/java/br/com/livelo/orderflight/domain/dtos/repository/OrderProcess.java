package br.com.livelo.orderflight.domain.dtos.repository;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProcess {
    private String id;
    private String commerceOrderId;

    public static OrderProcess fromOrderEntity(OrderEntity order) {
        return new OrderProcess(order.getId(), order.getCommerceOrderId());
    }
}
