package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.confirmation.impl.ConfirmationServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @Mock
    private ConnectorPartnersProxy connectorPartnersProxy;

    @InjectMocks
    private ConfirmationServiceImpl confirmationService;

    @Test
    void shouldConfirmOrder() throws Exception {
        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class)))
                .thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(ConnectorConfirmOrderRequest.class)))
                .thenReturn(MockBuilder.connectorConfirmOrderResponse().getBody());
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any()))
                .thenReturn(MockBuilder.confirmOrderResponse());

        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id",
                MockBuilder.confirmOrderRequest());
        assertEquals(MockBuilder.confirmOrderResponse(), confirmOrderResponse);
    }

    @Test
    void shouldThrowAnExceptionWhenOrderIsAlreadyConfirmed() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntityAlreadyConfirmed());
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest())).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.VALIDATION_ALREADY_CONFIRMED.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldThrowAnExceptionWhenPriceIsDifferent() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            OrderEntity foundOrder = MockBuilder.orderEntity();
            foundOrder.getPrice().setPointsAmount(BigDecimal.valueOf(2000));

            when(orderService.getOrderById(anyString())).thenReturn(foundOrder);
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest())).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.VALIDATION_OBJECTS_NOT_EQUAL.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldThrowAnExceptionWhenCommerceOrderIdsAreDifferent() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            OrderEntity foundOrder = MockBuilder.orderEntity();
            foundOrder.setCommerceOrderId("wrongId");

            when(orderService.getOrderById(anyString())).thenReturn(foundOrder);
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest())).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.VALIDATION_OBJECTS_NOT_EQUAL.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldUpdateOrderWithStatusFailedAndSave() throws Exception {
        ConfirmOrderResponse responseWithFailedStatus = MockBuilder.confirmOrderResponse();
        responseWithFailedStatus.setStatus(MockBuilder.confirmOrderStatusFailed());

        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class)))
                .thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(ConnectorConfirmOrderRequest.class)))
                .thenThrow(Exception.class);
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any())).thenReturn(responseWithFailedStatus);
        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id",
                MockBuilder.confirmOrderRequest());
        assertEquals(MockBuilder.confirmOrderResponseWithFailed(), confirmOrderResponse);
    }
}