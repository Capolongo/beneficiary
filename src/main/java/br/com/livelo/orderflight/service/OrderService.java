package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    public OrderEntity getOrder(String id) throws Exception {
        Optional<OrderEntity> op = orderRepository.findById(id);
        if(op.isPresent()){
            return op.get();
        }
        throw new Exception("Order NotFount");
    }
}
