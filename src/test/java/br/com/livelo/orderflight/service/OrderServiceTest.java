package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderExceptions.OrderNotFoundException;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnFoundOrderById() throws OrderNotFoundException {
        Optional<OrderEntity> mockedOrder = Optional.of(MockBuilder.orderEntity());
        when(orderRepository.findById(anyString())).thenReturn(mockedOrder);
        OrderEntity order = orderService.getOrderById("id");
        assertEquals(mockedOrder.get(), order);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderNotFound() throws OrderNotFoundException {
        when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById("id");
        });
    }

    @Test
    void shouldAddNewStatusToOrder() {
        OrderEntity order = MockBuilder.orderEntity();
        OrderStatusEntity status = MockBuilder.statusFailed();

        when(confirmOrderMapper
                .ConnectorConfirmOrderStatusResponseToStatusEntity(any(ConnectorConfirmOrderStatusResponse.class)))
                .thenReturn(status);

        OrderEntity updatedOrder = orderService.addNewOrderStatus(order,
                MockBuilder.connectorConfirmOrderStatusResponse());

        assertTrue(updatedOrder.getCurrentStatus().equals(status));
    }

    @Test
    void shouldFindOrderByCommerceOrderId() {
        var orderMock = mock(OrderEntity.class);
        when(this.orderRepository.findByCommerceOrderId(anyString())).thenReturn(Optional.of(orderMock));
        var response = this.orderService.findByCommerceOrderId("123");

        assertAll(
                () -> assertTrue(response.isPresent()),
                () -> assertInstanceOf(OrderEntity.class, response.get()));
    }

    @Test
    void shouldntFindOrderByCommerceOrderId() {
        when(this.orderRepository.findByCommerceOrderId(anyString())).thenReturn(Optional.empty());
        var response = this.orderService.findByCommerceOrderId("123");

        assertTrue(!response.isPresent());
    }

    @Test
    void shouldDeleteOrder() {
        var orderMock = mock(OrderEntity.class);
        doNothing().when(orderRepository).delete(any(OrderEntity.class));
        this.orderService.delete(orderMock);

        verify(orderRepository, times(1)).delete(orderMock);
    }

    @Test
    void shouldSaveOrder() {
        var orderMock = mock(OrderEntity.class);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderMock);
        var response = this.orderService.save(orderMock);

        assertNotNull(response);
    }
}
