package br.com.livelo.orderflight.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEntityTest {

    @Test
    void shouldSetStatus() {
        var data = LocalDateTime.now();
        var orderStatus = OrderStatusEntity.builder().statusDate(data).build();
        var expected = OrderStatusEntity.builder().statusDate(data.plusDays(1)).build();

        var order = OrderEntity.builder().statusHistory(Set.of(orderStatus, expected)).build();
        order.setStatus(order.getStatusHistory());
        assertEquals(expected, order.getStatus());
    }
}
