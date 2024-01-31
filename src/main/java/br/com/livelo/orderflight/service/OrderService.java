package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
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
        order.getStatusHistory().add(confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(status));
        order.setCurrentStatus(confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(status));

        return orderRepository.save(order);
    }
}