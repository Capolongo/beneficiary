package br.com.livelo.orderflight.service.completed;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.domain.entity.ProcessCounterEntity;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.completed.impl.CompletedServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletedServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @InjectMocks
    private CompletedServiceImpl completedServiceImpl;

    @Test
    void shouldChangeStatusSuccessfully() {
        OrderEntity order = buildOrderEntity(StatusConstants.VOUCHER_SENT.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);

        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        when(orderService.save(any(OrderEntity.class))).thenReturn(order);

        completedServiceImpl.orderProcess(orderProcess);

        verify(orderService, times(1)).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        verify(orderService, times(1)).save(any(OrderEntity.class));
        verifyNoMoreInteractions(orderService);

    }

    @Test
    void shouldFinishOrderProcessBecauseIsNotTheSameStatus() {
        OrderEntity order = buildOrderEntity(StatusConstants.WAIT_VOUCHER.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(false);

        completedServiceImpl.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusConstants.VOUCHER_SENT.getCode(), order.getCurrentStatus().getCode());
        verify(orderService, times(0)).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));

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