package br.com.livelo.orderflight.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderEntityTest {

    @Test
    void shouldSetStatus() {
        var data = LocalDateTime.now();
        var orderStatus = OrderStatusEntity.builder().id(1L).statusDate(data).build();
        var expected = OrderStatusEntity.builder().id(2L).statusDate(data.plusDays(1)).build();

        var order = OrderEntity.builder().statusHistory(Set.of(orderStatus, expected)).build();
        order.setStatus(orderStatus);
        assertEquals(orderStatus, order.getStatus());
    }
}
