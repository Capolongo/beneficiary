package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM ORDER_FLIGHT.ORDERS o " +
            "WHERE o.COMMERCE_ORDER_ID IN (:commerceOrderId)" +
            "AND o.EXPIRATION_DATE > TO_TIMESTAMP_TZ(:expirationDate, 'YYYY-MM-DD HH24:MI:SS TZR')")
    Optional<OrderEntity> findByCommerceOrderIdInAndExpirationDateAfter(List<String> commerceOrderId, String expirationDate);

    Page<OrderProcess> findAllByCurrentStatusCode(String statusCode, Pageable pageable);

    Page<OrderProcess> findAllByCurrentStatusCodeAndArrivalDateLessThan(String statusCode, LocalDateTime expirationDate, Pageable pageable);
}
