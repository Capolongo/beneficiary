package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderPriceRepository;
import br.com.livelo.orderflight.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderPriceRepository orderPriceRepository;

    @Transactional
    public void save(OrderEntity orderEntity) {
        var orderPriceEntity = orderPriceRepository.save(orderEntity.getOrderPriceEntity());
        orderEntity.setOrderPriceEntity(orderPriceEntity);
        orderRepository.save(orderEntity);
    }

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }
}
