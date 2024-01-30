package br.com.livelo.orderflight.mapper;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfirmOrderMapperTest {
    ConfirmOrderMapper confirmOrderMapper = Mappers.getMapper(ConfirmOrderMapper .class);

    @Test
    void shouldMapOrderEntityToConfirmOrderResponse() {
        OrderEntity order = MockBuilder.orderEntity();
        ConfirmOrderResponse confirmOrderResponse = MockBuilder.confirmOrderResponse();
        confirmOrderResponse.setExpirationDate(order.getExpirationDate().toString());
        confirmOrderResponse.setSubmittedDate(order.getSubmittedDate().toString());

        ConfirmOrderResponse mappedConfirmOrderResponse = confirmOrderMapper.orderEntityToConfirmOrderResponse(order);

        assertEquals(mappedConfirmOrderResponse, confirmOrderResponse);
    }
}