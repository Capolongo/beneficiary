package br.com.livelo.orderflight.service.installment;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.constants.PaymentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.installment.impl.InstallmentServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.payment.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private InstallmentOptionConstant installmentOptionConstant;

    @InjectMocks
    private InstallmentServiceImpl installmentService;

    @Test
    void shouldSucessGetInstallmentOptions() throws Exception {

        when(orderService.getOrderById(eq("lt1"))).thenReturn(MockBuilder.orderEntity());

        when(installmentOptionConstant.getCurrency()).thenReturn("PTS");
        when(installmentOptionConstant.getId()).thenReturn("lt1");
        when(installmentOptionConstant.getInterest()).thenReturn(new BigDecimal(1));
        when(installmentOptionConstant.getParcels()).thenReturn(1);

        InstallmentOptionsResponse response = installmentService.getInstallmentOptions("lt1", "1");

        assertEquals(response.getInstallmentOptions().getFirst().getId(), "lt1");
        assertEquals(response.getInstallmentOptions().getFirst().getAmount(), new BigDecimal(1000));
        assertEquals(response.getInstallmentOptions().getFirst().getCurrency(), "PTS");
        assertEquals(response.getInstallmentOptions().getFirst().getInterest(), new BigDecimal(1));
        assertEquals(response.getInstallmentOptions().getFirst().getParcels(), 1);

    }

}