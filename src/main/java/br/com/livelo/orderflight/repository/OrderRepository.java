package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.domain.dtos.repository.FindAllOrdersByStatusCode;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId);

    List<FindAllOrdersByStatusCode> findAllByCurrentStatusCode(String statusCode, Pageable pageable);
}
