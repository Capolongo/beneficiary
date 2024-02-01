package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ConfirmOrderMapper confirmOrderMapper;

    public OrderEntity getOrderById(String id) throws Exception {
        Optional<OrderEntity> order = orderRepository.findById(id);

        if (order.isEmpty()) {
            throw new Exception("Order not found");
        }

        return order.get();
    }

    public OrderEntity addNewOrderStatus(OrderEntity order, ConnectorConfirmOrderStatusResponse status) {
        OrderStatusEntity mappedStatus = confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(status);
        order.getStatusHistory().add(mappedStatus);
        order.setCurrentStatus(mappedStatus);

        return order;
    }

    public Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId) {
        return this.orderRepository.findByCommerceOrderId(commerceOrderId);
    }

    public void delete(OrderEntity order) {
        this.orderRepository.delete(order);
    }

    public OrderEntity save(OrderEntity order) {
        return this.orderRepository.save(order);
    }
}
