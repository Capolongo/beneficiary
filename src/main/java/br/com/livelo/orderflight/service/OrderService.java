package br.com.livelo.orderflight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public OrderEntity save(OrderEntity orderEntity) {
		return orderRepository.save(orderEntity);
	}

	public List<OrderEntity> findAll() {
		
		var orders = orderRepository.findAll();
		
		return orders;
	}
}
