package br.com.livelo.orderflight.service.voucher;

import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.voucher.impl.VoucherServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;

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
        OrderEntity order = buildOrderEntity(StatusLivelo.WAIT_VOUCHER.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString()))
                .thenReturn(ProcessCounterEntity.builder()
                        .process(Webhooks.VOUCHER.value)
                        .createDate(ZonedDateTime.now())
                .count(0).build());
        when(connectorPartnersProxy.getVoucherOnPartner(anyString(), anyString(), anyString()))
                .thenReturn(PartnerConfirmOrderResponse.builder()
                        .currentStatus(PartnerConfirmOrderStatusResponse.builder().build())
                        .voucher("https://fake-url.com")
                        .build());
        when(confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(any(PartnerConfirmOrderStatusResponse.class)))
                .thenReturn(OrderCurrentStatusEntity.builder().code(StatusLivelo.WAIT_VOUCHER.getCode()).build());
        when(orderService.getFlightFromOrderItems(any())).thenReturn(OrderItemEntity.builder().build());
        doNothing().when(orderService).incrementProcessCounter(any(ProcessCounterEntity.class));
        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderCurrentStatusEntity.class));
        doNothing().when(orderService).updateVoucher(any(), anyString());
        when(orderService.save(any(OrderEntity.class))).thenReturn(order);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).addNewOrderStatus(any(OrderEntity.class), any(OrderCurrentStatusEntity.class));
        verify(orderService, times(1)).incrementProcessCounter(any(ProcessCounterEntity.class));
        verify(orderService, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void shouldSetOrderFailedBecauseMaxProcessExtrapolate() {
        OrderEntity order = buildOrderEntity(StatusLivelo.WAIT_VOUCHER.getCode());
        OrderProcess orderProcess = buildOrderProcess();

        ProcessCounterEntity processCounter = ProcessCounterEntity.builder()
                .count(193)
                .build();

        OrderCurrentStatusEntity statusFailed = MockBuilder.statusFailed();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString())).thenReturn(processCounter);
        when(orderService.buildOrderStatusFailed(anyString())).thenReturn(statusFailed);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusLivelo.WAIT_VOUCHER.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, times(1)).buildOrderStatusFailed("O contador excedeu o limite de tentativas");
    }

    @Test
    void shouldFinishOrderProcessBecauseIsNotTheSameStatus() {
        String process = Webhooks.VOUCHER.value;
        OrderEntity order = buildOrderEntity(StatusLivelo.PROCESSING.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(false);

        voucherService.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusLivelo.WAIT_VOUCHER.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, never()).getProcessCounter(order, process);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void shouldSetOrderFailedBecauseThrowException() {
        OrderEntity order = buildOrderEntity(StatusLivelo.WAIT_VOUCHER.getCode());
        order.setPartnerOrderId(null);

        ProcessCounterEntity counter = ProcessCounterEntity.builder().build();
        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);
        when(orderService.getProcessCounter(any(OrderEntity.class), anyString())).thenReturn(counter);
        voucherService.orderProcess(orderProcess);

        verify(confirmOrderMapper, times(0)).connectorConfirmOrderStatusResponseToStatusEntity(any());
        verify(orderService, times(0)).getFlightFromOrderItems(any());
        verify(orderService, times(1)).buildOrderStatusFailed(anyString());
    }

    private OrderEntity buildOrderEntity(String status) {
        return OrderEntity.builder()
                .id("lf1")
                .partnerCode("CVC")
                .partnerOrderId("partnerOrderId")
                .currentStatus(OrderCurrentStatusEntity.builder()
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