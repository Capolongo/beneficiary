package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.entities.OrderPriceEntity;
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
        OrderPriceEntity orderPriceEntity = orderEntity.getOrderPriceEntity();

        var orderPrice = orderPriceRepository.save(orderPriceEntity);

        orderPriceEntity.setId(null);
        orderEntity.setOrderPriceEntity(orderPrice);
        orderEntity.setId(null);
        orderRepository.save(orderEntity);
    }

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }
}
