package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}
