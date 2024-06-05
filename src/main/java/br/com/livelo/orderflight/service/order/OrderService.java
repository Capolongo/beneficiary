package br.com.livelo.orderflight.service.order;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderEntity getOrderById(String id) throws OrderFlightException;

    PaginationOrderProcessResponse getOrdersByStatusCode(String status, Optional<String> limitArrivalDate, Integer page, Integer rows)
            throws OrderFlightException;

    void addNewOrderStatus(OrderEntity order, OrderCurrentStatusEntity status);

    boolean isFlightItem(OrderItemEntity item);

    Optional<OrderEntity> findByCommerceOrderIdInAndExpirationDateAfter(List<String> commerceOrderId);

    void delete(OrderEntity order);

    OrderEntity save(OrderEntity order);

    OrderItemEntity findByCommerceItemIdAndSkuId(String commerceItemId, SkuItemResponse skuItemResponseDTO);
}
