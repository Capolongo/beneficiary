package br.com.livelo.orderflight.service.validation;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.validation.impl.OrderValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class OrderValidatorServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderValidatorServiceImpl orderValidatorService;


    @Test
    void shouldValidOrder() {
        OrderEntity order = MockBuilder.orderEntity();
        Optional<OrderEntity> mockedOrder = Optional.of(order);
        OrderValidateRequestDTO orderValidateRequest = MockBuilder.orderValidateRequest();

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("id"))).thenReturn(mockedOrder);

        var response = orderValidatorService.validateOrder(orderValidateRequest);
        assertInstanceOf(OrderValidateResponseDTO.class, response);
    }
    @Test
    void shouldThrowErrorWhenTryToValidOrder() {
        OrderEntity order = null;
        OrderValidateRequestDTO orderValidateRequest = MockBuilder.orderValidateRequest();

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("id"))).thenReturn(Optional.empty());

        assertThrows(OrderFlightException.class, () -> {
            orderValidatorService.validateOrder(orderValidateRequest);
        });
    }

}