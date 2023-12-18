package br.com.livelo.orderflight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.livelo.orderflight.entities.OrderItemEntity;
import br.com.livelo.orderflight.repository.OrderItemPriceRepository;
import br.com.livelo.orderflight.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderItemPriceRepository orderItemPriceRepository;

    @Transactional
    public void save(OrderItemEntity orderItemEntity) {
        var orderItemPriceEntity = orderItemPriceRepository.save(orderItemEntity.getOrderItemPriceEntity());
        orderItemEntity.setOrderItemPriceEntity(orderItemPriceEntity);
        orderItemRepository.save(orderItemEntity);
    }

    public List<OrderItemEntity> findAll() {
        return orderItemRepository.findAll();
    }
}