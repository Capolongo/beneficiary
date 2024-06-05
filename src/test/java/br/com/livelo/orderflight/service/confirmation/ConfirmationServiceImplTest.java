package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.PartnerConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.headers.RequiredHeaders;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.LiveloPartnersMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.proxies.LiveloPartnersProxy;
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
    @Mock
    private LiveloPartnersProxy liveloPartnersProxy;
    @Mock
    private LiveloPartnersMapper liveloPartnersMapper;
    @InjectMocks
    private ConfirmationServiceImpl confirmationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(confirmationService, "getConfirmationMaxProcessCountFailed", 48);
    }

    @Test
    void shouldConfirmOrder() throws Exception {
        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class)))
                .thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(PartnerConfirmOrderStatusResponse.class)))
                .thenReturn(MockBuilder.statusProcessing());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(PartnerConfirmOrderRequest.class), any(RequiredHeaders.class)))
                .thenReturn(MockBuilder.connectorConfirmOrderResponse().getBody());
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any()))
                .thenReturn(MockBuilder.confirmOrderResponse());

        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id",
                MockBuilder.confirmOrderRequest(), new RequiredHeaders("", ""));
        assertEquals(MockBuilder.confirmOrderResponse(), confirmOrderResponse);
    }

    @Test
    void shouldThrowAnExceptionWhenOrderIsAlreadyConfirmed() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntityAlreadyConfirmed());
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest(), new RequiredHeaders("", ""))).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONFIRMATION_ALREADY_CONFIRMED_ERROR.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldThrowAnExceptionWhenPriceIsDifferent() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            OrderEntity foundOrder = MockBuilder.orderEntity();
            foundOrder.getPrice().setPointsAmount(BigDecimal.valueOf(2000));

            when(orderService.getOrderById(anyString())).thenReturn(foundOrder);
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest(), any(RequiredHeaders.class))).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONFIRMATION_ORDER_VALIDATION_ERROR.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldThrowAnExceptionWhenCommerceOrderIdsAreDifferent() {
        try {
            Exception exception = Mockito.mock(Exception.class);
            OrderEntity foundOrder = MockBuilder.orderEntity();
            foundOrder.setCommerceOrderId("wrongId");

            when(orderService.getOrderById(anyString())).thenReturn(foundOrder);
            when(confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest(), any(RequiredHeaders.class))).thenThrow(exception);

        } catch (Exception exception) {
            assertEquals(OrderFlightErrorType.ORDER_FLIGHT_CONFIRMATION_ORDER_VALIDATION_ERROR.getTitle(), exception.getMessage());
        }
    }

    @Test
    void shouldUpdateOrderWithStatusFailedAndSave() throws OrderFlightException {
        ConfirmOrderResponse responseWithFailedStatus = MockBuilder.confirmOrderResponse();
        responseWithFailedStatus.setStatus(MockBuilder.confirmOrderStatusFailed());

        when(orderService.getOrderById(anyString())).thenReturn(MockBuilder.orderEntity());
        when(confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(any(OrderEntity.class))).thenReturn(MockBuilder.connectorConfirmOrderRequest());
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(PartnerConfirmOrderStatusResponse.class))).thenReturn(MockBuilder.statusProcessing());
        when(connectorPartnersProxy.confirmOnPartner(anyString(), any(PartnerConfirmOrderRequest.class), any(RequiredHeaders.class))).thenThrow(FeignException.class);
        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any())).thenReturn(responseWithFailedStatus);
        ConfirmOrderResponse confirmOrderResponse = confirmationService.confirmOrder("id", MockBuilder.confirmOrderRequest(), new RequiredHeaders("", ""));
        assertEquals(MockBuilder.confirmOrderResponseWithFailed(), confirmOrderResponse);
    }

    @Test
    void shouldCallProcessOrderTimeDifference() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderCurrentStatusEntity statusProcessing = MockBuilder.statusProcessing();
        statusProcessing.setCreateDate(ZonedDateTime.now());

        OrderEntity order = MockBuilder.orderEntity();
        order.setCurrentStatus(statusProcessing);
        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        PartnerConfirmOrderResponse connectorConfirmOrderResponse = MockBuilder.connectorConfirmOrderResponse().getBody();
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(1, process);
        OrderItemEntity itemFlight = MockBuilder.orderItemEntity();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true).thenReturn(false);
        when(orderService.getProcessCounter(order, process)).thenReturn(processCounter);
        when(connectorPartnersProxy.getConfirmationOnPartner(anyString(), anyString(), anyString())).thenReturn(connectorConfirmOrderResponse);
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(PartnerConfirmOrderStatusResponse.class))).thenReturn(statusProcessing);
        when(orderService.getFlightFromOrderItems(any())).thenReturn(itemFlight);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(2)).isSameStatus(StatusLivelo.PROCESSING.getCode(), order.getCurrentStatus().getCode());
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
        verify(orderService, times(1)).isSameStatus(StatusLivelo.PROCESSING.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, never()).getProcessCounter(order, process);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void shouldSetOrderFailedBecauseMaxProcessExtrapolate() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderEntity order = MockBuilder.orderEntity();

        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(48, process);
        OrderCurrentStatusEntity statusFailed = MockBuilder.statusFailed();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString())).thenReturn(processCounter);
        when(orderService.buildOrderStatusFailed(anyString())).thenReturn(statusFailed);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusLivelo.PROCESSING.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, times(1)).buildOrderStatusFailed("O contador excedeu o limite de tentativas");
    }

    @Test
    void shouldOrderProcessSuccess() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderCurrentStatusEntity statusProcessing = MockBuilder.statusProcessing();

        OrderEntity order = MockBuilder.orderEntity();
        order.setCurrentStatus(statusProcessing);

        OrderProcess orderProcess = MockBuilder.listOfOrderProcess(1).get(0);
        PartnerConfirmOrderResponse connectorConfirmOrderResponse = MockBuilder.connectorConfirmOrderResponse().getBody();
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(1, process);
        OrderItemEntity itemFlight = MockBuilder.orderItemEntity();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(order, process)).thenReturn(processCounter);
        when(connectorPartnersProxy.getConfirmationOnPartner(anyString(), anyString(), anyString())).thenReturn(connectorConfirmOrderResponse);
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(PartnerConfirmOrderStatusResponse.class))).thenReturn(statusProcessing);
        when(orderService.getFlightFromOrderItems(any())).thenReturn(itemFlight);

        confirmationService.orderProcess(orderProcess);

        verify(orderService, times(1)).updateVoucher(itemFlight, connectorConfirmOrderResponse.getVoucher());
        verify(orderService, times(1)).incrementProcessCounter(processCounter);
        verify(orderService, times(1)).addNewOrderStatus(order, statusProcessing);
        verify(orderService, times(1)).save(order);
        verify(orderService, times(1)).updateOrderOnLiveloPartners(order, statusProcessing.getCode());
        verify(orderService, times(1)).orderDetailLog("orderProcess", statusProcessing.getCode(), order);
        verifyNoMoreInteractions(orderService);
    }
}