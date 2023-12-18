package br.com.livelo.orderflight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderItemRepository;
import br.com.livelo.orderflight.repository.OrderPriceRepository;
import br.com.livelo.orderflight.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderPriceRepository orderPriceRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Transactional
	public void save(OrderEntity orderEntity) {
		var orderPriceEntity = orderPriceRepository.save(orderEntity.getOrderPriceEntity());
		orderEntity.setOrderPriceEntity(orderPriceEntity);
		orderRepository.save(orderEntity);
		var order = orderRepository.save(orderEntity);
		System.out.println(order);
//		orderItemRepository.save(null)
	}

	public List<OrderEntity> findAll() {
		return orderRepository.findAll();
	}
}
