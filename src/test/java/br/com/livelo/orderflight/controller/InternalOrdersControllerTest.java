package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class InternalOrdersControllerTest {
    @InjectMocks
    private InternalOrdersController controller;
    @Mock
    private OrderRepository orderRepository;

    @Test
    void shouldCreateOrder() {
        var expected = mock(OrderEntity.class);
        Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(expected);

        var response = controller.createOrder(expected);
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertEquals(expected, response.getBody())
        );
    }
    
    @Test
    void shouldRetrieveOrder() {
        Mockito.when(orderRepository.findById(anyString())).thenReturn(Optional.of(new OrderEntity()));

        var response = controller.getById("lf1");
        assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertNotNull(response),
                () -> assertInstanceOf(OrderEntity.class, response.getBody())
        );
    }
}







