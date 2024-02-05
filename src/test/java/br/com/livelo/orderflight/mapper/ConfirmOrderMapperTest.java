package br.com.livelo.orderflight.mapper;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfirmOrderMapperTest {
    ConfirmOrderMapper confirmOrderMapper = Mappers.getMapper(ConfirmOrderMapper .class);

    @Test
    void shouldMapOrderEntityToConfirmOrderResponse() {
        String date = LocalDateTime.parse("2024-01-30T14:13:46.406151").toString();
        OrderEntity order = MockBuilder.orderEntity();
        ConfirmOrderResponse confirmOrderResponse = MockBuilder.confirmOrderResponse();
        ConfirmOrderResponse mappedConfirmOrderResponse = confirmOrderMapper.orderEntityToConfirmOrderResponse(order);

        confirmOrderResponse.setExpirationDate(date);
        confirmOrderResponse.setSubmittedDate(date);
        mappedConfirmOrderResponse.setSubmittedDate(date);
        mappedConfirmOrderResponse.setExpirationDate(date);

        assertEquals(confirmOrderResponse, mappedConfirmOrderResponse);
    }

    @Test
    void shouldMapOrderEntityToConnectorConfirmOrderRequest() {
        OrderEntity order = MockBuilder.orderEntity();
        String date = LocalDateTime.parse("2024-01-30T14:13:46.406151").toString();
        ConnectorConfirmOrderRequest connectorConfirmOrderRequest = MockBuilder.connectorConfirmOrderRequest();
        ConnectorConfirmOrderRequest mappedConfirmOrderResponse = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(order);

        connectorConfirmOrderRequest.setExpirationDate(date);
        connectorConfirmOrderRequest.setSubmittedDate(date);
        mappedConfirmOrderResponse.setSubmittedDate(date);
        mappedConfirmOrderResponse.setExpirationDate(date);


        assertEquals(connectorConfirmOrderRequest, mappedConfirmOrderResponse);
    }

    @Test
    void shouldMapConnectorConfirmOrderStatusResponseToStatusEntity() {
        LocalDateTime date = LocalDateTime.now();
        ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse =  MockBuilder.connectorConfirmOrderStatusResponse();
        OrderStatusEntity mappedOrderStatusEntity = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderStatusResponse);
        OrderStatusEntity orderStatusEntity = MockBuilder.statusInitial();

        orderStatusEntity.setStatusDate(date);
        mappedOrderStatusEntity.setStatusDate(date);

        assertEquals(orderStatusEntity, mappedOrderStatusEntity);
    }
}