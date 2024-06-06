package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<OrderItemEntity, Long> {
        Optional<OrderItemEntity> findFirstByCommerceItemIdAndSkuIdOrderByCreateDateDesc(String commerceOrderId, String skuId);
}
