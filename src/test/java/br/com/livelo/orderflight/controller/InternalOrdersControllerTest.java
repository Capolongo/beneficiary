package br.com.livelo.orderflight.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Optional;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class InternalOrdersControllerTest {
    @InjectMocks
    private InternalOrdersController controller;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void shouldReturnCreateOrderSuccess() {
        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order(null).get());
        ResponseEntity<OrderEntity> order = controller.createOrder(order(null).get());
        assertNotNull(order);
        assertEquals(201, order.getStatusCode().value());
        verify(orderRepository).save(any(OrderEntity.class));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnGetOrderSuccess() {
        final String id = "lf1";
        Mockito.when(orderRepository.findById(Mockito.anyString())).thenReturn(order(id));
        ResponseEntity<OrderEntity> order = controller.getById(id);
        assertNotNull(order);
        assertEquals(200, order.getStatusCode().value());
        verify(orderRepository).findById(anyString());
        verifyNoMoreInteractions(orderRepository);
    }

    private Optional<OrderEntity> order(String id) {
        return Optional.of(OrderEntity.builder().commerceOrderId("1").originOrder("1").id(id).partnerOrderId("1")
                .channel("1").customerIdentifier("1")
                .build());
    }

    void shouldCreateOrder() {
        var expected = mock(OrderEntity.class);
        Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(expected);

        var response = controller.createOrder(expected);
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertEquals(expected, response.getBody()));
    }

    @Test
    void shouldRetrieveOrder() {
        Mockito.when(orderRepository.findById(anyString())).thenReturn(Optional.of(new OrderEntity()));

        var response = controller.getById("lf1");
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertInstanceOf(OrderEntity.class, response.getBody()));
    }
}
