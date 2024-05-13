package br.com.livelo.orderflight.service.completed;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.service.completed.impl.CompletedServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        OrderEntity order = buildOrderEntity(StatusLivelo.VOUCHER_SENT.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);

        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        when(orderService.save(any(OrderEntity.class))).thenReturn(order);
        doNothing().when(orderService).updateOrderOnLiveloPartners(any(OrderEntity.class), anyString());

        completedServiceImpl.orderProcess(orderProcess);

        verify(orderService, times(1)).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));
        verify(orderService, times(1)).save(any(OrderEntity.class));
        verify(orderService, times(1)).updateOrderOnLiveloPartners(any(OrderEntity.class), anyString());
        verifyNoMoreInteractions(orderService);

    }

    @Test
    void shouldFinishOrderProcessBecauseIsNotTheSameStatus() {
        OrderEntity order = buildOrderEntity(StatusLivelo.WAIT_VOUCHER.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(false);

        completedServiceImpl.orderProcess(orderProcess);

        verify(orderService, times(1)).getOrderById(anyString());
        verify(orderService, times(1)).isSameStatus(StatusLivelo.VOUCHER_SENT.getCode(), order.getCurrentStatus().getCode());
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