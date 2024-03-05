package br.com.livelo.orderflight.service.voucher;

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
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.voucher.impl.VoucherServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @Mock
    private ConnectorPartnersProxy connectorPartnersProxy;
    @InjectMocks
    private VoucherServiceImpl voucherService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(voucherService, "errorCount", 192);
    }

    @Test
    void shouldChangeStatusSuccessfully() {
        OrderEntity order = buildOrderEntity(StatusConstants.WAIT_VOUCHER.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString()))
                .thenReturn(ProcessCounterEntity.builder()
                        .process(Webhooks.VOUCHER.value)
                        .createDate(ZonedDateTime.now())
                .count(0).build());
        when(connectorPartnersProxy.getVoucherOnPartner(anyString(), anyString(), anyString()))
                .thenReturn(ConnectorConfirmOrderResponse.builder()
                        .currentStatus(ConnectorConfirmOrderStatusResponse.builder().build())
                        .voucher("https://fake-url.com")
                        .voucher("https://fake-url.com")
                        .build());
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(ConnectorConfirmOrderStatusResponse.class)))
                .thenReturn(OrderStatusEntity.builder().code(StatusConstants.WAIT_VOUCHER.getCode()).build());
        when(orderService.getFlightFromOrderItems(any())).thenReturn(OrderItemEntity.builder().build());
        doNothing().when(orderService).incrementProcessCounter(any(ProcessCounterEntity.class));
        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        doNothing().when(orderService).updateVoucher(any(), anyString());
        when(orderService.save(any(OrderEntity.class))).thenReturn(order);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        verify(orderService, times(1)).incrementProcessCounter(any(ProcessCounterEntity.class));
        verify(orderService, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void shouldSetOrderFailedBecauseMaxProcessExtrapolate() {
        OrderEntity order = buildOrderEntity(StatusConstants.WAIT_VOUCHER.getCode());
        OrderProcess orderProcess = buildOrderProcess();

        ProcessCounterEntity processCounter = ProcessCounterEntity.builder()
                .count(193)
                .build();

        OrderStatusEntity statusFailed = MockBuilder.statusFailed();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString())).thenReturn(processCounter);
        when(orderService.buildOrderStatusFailed(anyString())).thenReturn(statusFailed);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusConstants.WAIT_VOUCHER.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, times(1)).buildOrderStatusFailed("O contador excedeu o limite de tentativas");
    }

    @Test
    void shouldFinishOrderProcessBecauseIsNotTheSameStatus() {
        String process = Webhooks.VOUCHER.value;
        OrderEntity order = buildOrderEntity(StatusConstants.PROCESSING.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(false);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusConstants.WAIT_VOUCHER.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, never()).getProcessCounter(order, process);

        verifyNoMoreInteractions(orderService);
    }

    private OrderEntity buildOrderEntity(String status) {
        return OrderEntity.builder()
                .id("lf1")
                .partnerCode("CVC")
                .partnerOrderId("partnerOrderId")
                .currentStatus(OrderStatusEntity.builder()
                        .code(status)
                        .build()).build();
    }

    private OrderProcess buildOrderProcess() {
        return OrderProcess.builder()
                .id("lf1")
                .commerceOrderId("teste")
                .build();
    }
}