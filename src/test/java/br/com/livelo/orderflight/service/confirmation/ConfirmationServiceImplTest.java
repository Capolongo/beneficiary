package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.domain.entity.ProcessCounterEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.confirmation.impl.ConfirmationServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import feign.FeignException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(confirmationService, "maxProcessCountFailed", 48);
    }

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
    void shouldUpdateOrderWithStatusFailedAndSave() throws OrderFlightException {
        ConfirmOrderResponse responseWithFailedStatus = MockBuilder.confirmOrderResponse();
        responseWithFailedStatus.setStatus(MockBuilder.confirmOrderStatusFailed());

        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class))).thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(ConnectorConfirmOrderRequest.class))).thenThrow(FeignException.class);
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any())).thenReturn(responseWithFailedStatus);
        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest());
        assertEquals(MockBuilder.confirmOrderResponseWithFailed(), confirmOrderResponse);
    }

    @Test
    void shouldCallProcessOrderTimeDifference() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderStatusEntity statusProcessing = MockBuilder.statusProcessing();
        statusProcessing.setCreateDate(ZonedDateTime.now());

        OrderEntity order = MockBuilder.orderEntity();
        order.setCurrentStatus(statusProcessing);
        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        ConnectorConfirmOrderResponse connectorConfirmOrderResponse = MockBuilder.connectorConfirmOrderResponse().getBody();
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(1, process);
        OrderItemEntity itemFlight = MockBuilder.orderItemEntity();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true).thenReturn(false);
        when(orderService.getProcessCounter(order, process)).thenReturn(processCounter);
        when(connectorPartnersProxy.getConfirmationOnPartner(anyString(), anyString())).thenReturn(connectorConfirmOrderResponse);
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(ConnectorConfirmOrderStatusResponse.class))).thenReturn(statusProcessing);
        when(orderService.getFlightFromOrderItems(any())).thenReturn(itemFlight);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(2)).isSameStatus(StatusConstants.PROCESSING.getCode(), order.getCurrentStatus().getCode());
    }

    @Test
    void shouldFinishOrderProcessBecauseIsNotTheSameStatus() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderEntity order = MockBuilder.orderEntity();

        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(false);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusConstants.PROCESSING.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, never()).getProcessCounter(order, process);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void shouldSetOrderFailedBecauseMaxProcessExtrapolate() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderEntity order = MockBuilder.orderEntity();

        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(48, process);
        OrderStatusEntity statusFailed = MockBuilder.statusFailed();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString())).thenReturn(processCounter);
        when(orderService.buildOrderStatusFailed(anyString())).thenReturn(statusFailed);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusConstants.PROCESSING.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, times(1)).buildOrderStatusFailed("O contador excedeu o limite de tentativas");
    }

    @Test
    void shouldOrderProcessSuccess() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderStatusEntity statusProcessing = MockBuilder.statusProcessing();

        OrderEntity order = MockBuilder.orderEntity();
        order.setCurrentStatus(statusProcessing);

        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        ConnectorConfirmOrderResponse connectorConfirmOrderResponse = MockBuilder.connectorConfirmOrderResponse().getBody();
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(1, process);
        OrderItemEntity itemFlight = MockBuilder.orderItemEntity();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(order, process)).thenReturn(processCounter);
        when(connectorPartnersProxy.getConfirmationOnPartner(anyString(), anyString())).thenReturn(connectorConfirmOrderResponse);
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(ConnectorConfirmOrderStatusResponse.class))).thenReturn(statusProcessing);
        when(orderService.getFlightFromOrderItems(any())).thenReturn(itemFlight);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(1)).updateVoucher(itemFlight, connectorConfirmOrderResponse.getVoucher());
        verify(orderService, times(1)).incrementProcessCounter(processCounter);
        verify(orderService, times(1)).addNewOrderStatus(order, statusProcessing);
        verify(orderService, times(1)).save(order);
        verifyNoMoreInteractions(orderService);
    }
}