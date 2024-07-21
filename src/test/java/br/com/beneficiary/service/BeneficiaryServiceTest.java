package br.com.beneficiary.service;

import br.com.beneficiary.mappers.BeneficiaryMapper;
import br.com.beneficiary.repository.BeneficiarioRepository;
import br.com.beneficiary.repository.DocumentoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceTest {

    @Mock
    private BeneficiarioRepository beneficiarioRepository;
    @Mock
    private DocumentoRepository documRepository;
    @Mock
    private BeneficiaryMapper mapper;
    @InjectMocks
    private BeneficiaryService service;

    @Test
    void shouldChangeStatusSuccessfully() {


        service.createRecipient()

        OrderEntity order = buildOrderEntity(StatusLivelo.VOUCHER_SENT.getCode());

        OrderProcess orderProcess = buildOrderProcess();

        when(orderService.getOrderById(anyString())).thenReturn(order);
        when(orderService.isSameStatus(anyString(), anyString())).thenReturn(true);

        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderCurrentStatusEntity.class));
        when(orderService.save(any(OrderEntity.class))).thenReturn(order);
        doNothing().when(orderService).updateOrderOnLiveloPartners(any(OrderEntity.class), anyString());

        completedServiceImpl.orderProcess(orderProcess);

        verify(orderService, times(1)).addNewOrderStatus(any(OrderEntity.class), any(OrderCurrentStatusEntity.class));
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
        verify(orderService, times(0)).addNewOrderStatus(any(OrderEntity.class), any(OrderCurrentStatusEntity.class));

        verifyNoMoreInteractions(orderService);
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
