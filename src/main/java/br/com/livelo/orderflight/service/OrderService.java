package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderEntity getOrderById(String id) throws Exception {
        Optional<OrderEntity> order = orderRepository.findById(id);

        if (order.isEmpty()) {
            throw new Exception("Order NotFount");
        }

        return order.get();
    }
}