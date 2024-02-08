package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId);
}
