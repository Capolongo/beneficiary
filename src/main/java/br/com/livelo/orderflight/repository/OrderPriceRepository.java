package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.entities.OrderPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPriceRepository extends JpaRepository<OrderPriceEntity, Integer> {
}
