package br.com.livelo.orderflight.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderEntityTest {

    @Test
    void shouldSetStatus() {
        var orderStatus = OrderCurrentStatusEntity.builder().id(1L).build();
        var orderStatusHistory = OrderStatusHistoryEntity.builder().id(1L).build();
        var expected = OrderStatusHistoryEntity.builder().id(2L).build();

        var order = OrderEntity.builder().statusHistory(Set.of(orderStatusHistory, expected)).build();
        order.setCurrentStatus(orderStatus);
        assertEquals(orderStatus, order.getCurrentStatus());
    }
}
