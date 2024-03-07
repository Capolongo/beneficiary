package br.com.livelo.orderflight.repository;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId);

    Page<OrderProcess> findAllByCurrentStatusCode(String statusCode, Pageable pageable);

    @Query("select o.id, o.commerceOrderId from OrderEntity o " +
            " inner join o.currentStatus os " +
            " inner join o.items oi " +
            " inner join oi.segments s " +
            " where os.code = ?1 " +
            " group by o.id, o.commerceOrderId " +
            " having ?2 < max(s.arrivalDate) ")
    Page<OrderEntity> findAllByCurrentStatusCodeAndArrivalDateLessThan(String statusCode,
            LocalDateTime expirationDate, Pageable pageable);


}
