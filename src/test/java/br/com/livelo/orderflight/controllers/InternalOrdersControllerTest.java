package br.com.livelo.orderflight.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class InternalOrdersControllerTest {
    @InjectMocks
    private InternalOrdersController controller;
    @Mock
    private OrderRepository orderRepository;

    @Test
    public void createOrderSuccess() throws Exception {
        Mockito.when(orderRepository.save(Mockito.any(OrderEntity.class))).thenReturn(order(null).get());
        var order = controller.createOrder(order(null).get());
        assertNotNull(order);
    }

    @Test
    public void getOrderSuccess() throws Exception {
        final String id = "lf1";
        Mockito.when(orderRepository.findById(Mockito.anyString())).thenReturn(order(id));
        var order = controller.getById(id);
        assertNotNull(order);
    }

    private Optional<OrderEntity> order(String id) {
        return Optional.of(OrderEntity.builder().commerceOrderId("1").originOrder("1").id(id).partnerOrderId("1").channel("1").customerIdentifier("1")
                .build());
    }
}







