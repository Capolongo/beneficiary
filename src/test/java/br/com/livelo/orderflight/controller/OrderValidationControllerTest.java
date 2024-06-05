package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.validation.impl.OrderValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidationControllerTest {

    @Mock
    private OrderValidatorServiceImpl orderValidatorService;


    @InjectMocks
    private OrderValidationController controller;

    @Test
    void shouldReturnValidOrderWithStatus200() {
        OrderValidateResponseDTO responseBody = MockBuilder.orderValidateResponse();

        when(orderValidatorService.validateOrder(Mockito.any(OrderValidateRequestDTO.class))).thenReturn(responseBody);

        ResponseEntity<OrderValidateResponseDTO> response = controller.validateOrder(OrderValidateRequestDTO.builder().build());

        assertEquals(200, response.getStatusCode().value());
    }
}