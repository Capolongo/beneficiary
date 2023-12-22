package br.com.livelo.orderflight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.livelo.orderflight.entities.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

}
