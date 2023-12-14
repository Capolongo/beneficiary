package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.entities.OrderItemPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemPriceRepository extends JpaRepository<OrderItemPriceEntity, Integer> {
}
