package br.com.livelo.orderflight.service.payment;

import br.com.livelo.orderflight.constants.PaymentOptionConstant;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.payment.impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private PaymentOptionConstant paymentOptionConstant;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void shouldSucessGetPaymentOptions() throws Exception {

        when(orderService.getOrderById(eq("lt1"))).thenReturn(MockBuilder.orderEntity());

        when(paymentOptionConstant.getDescription()).thenReturn("default");
        when(paymentOptionConstant.getId()).thenReturn("lt1");
        when(paymentOptionConstant.getName()).thenReturn("default");

        var response = paymentService.getPaymentOptions("lt1", "1");

        assertEquals(response.getPaymentOptions().getFirst().getId(), "lt1");
        assertEquals(response.getPaymentOptions().getFirst().getDescription(), "default");
        assertEquals(response.getPaymentOptions().getFirst().getName(), "default");

    }

}