package br.com.livelo.orderflight.service.order;

import java.util.Optional;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.ReservationException;

public interface OrderService {
  OrderEntity getOrderById(String id) throws ReservationException;

  void addNewOrderStatus(OrderEntity order, OrderStatusEntity status);

  Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId);

  void delete(OrderEntity order);

  OrderEntity save(OrderEntity order);
}
