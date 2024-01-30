package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceTest {
    @Mock
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @Mock
    private ConnectorPartnersProxy connectorPartnersProxy;

    @InjectMocks
    private ConfirmationService confirmationService;

    @Test
    void shouldConfirmOrder() throws Exception {
        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class))).thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(ConnectorConfirmOrderRequest.class))).thenReturn(MockBuilder.connectorConfirmOrderResponse().getBody());
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any())).thenReturn(MockBuilder.confirmOrderResponse());

        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest());
        assertEquals(MockBuilder.confirmOrderResponse(), confirmOrderResponse);
    }

    @Test
    void shouldThrowAnExceptionWhenOrderIsAlreadyConfirmed() throws Exception {
        try {
            Exception exception = Mockito.mock(Exception.class);
            when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntityAlreadyConfirmed());
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest())).thenThrow(exception);


        } catch (Exception exception) {
            assertEquals("Order is already confirmed", exception.getMessage());
        }
    }
}