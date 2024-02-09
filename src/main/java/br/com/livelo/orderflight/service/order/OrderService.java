package br.com.livelo.orderflight.service.order;

import java.util.List;
import java.util.Optional;

import br.com.livelo.orderflight.domain.dtos.repository.FindAllOrdersByStatusCode;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;

public interface OrderService {
  OrderEntity getOrderById(String id) throws OrderFlightException;

  List<FindAllOrdersByStatusCode> getOrdersByStatusCode(String status, Integer page, Integer limit) throws OrderFlightException;

  void addNewOrderStatus(OrderEntity order, OrderStatusEntity status);

  Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId);

  void delete(OrderEntity order);

  OrderEntity save(OrderEntity order);
}
