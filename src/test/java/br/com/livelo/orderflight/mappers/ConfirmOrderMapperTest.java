package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderDocumentResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderFlightsLegsResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderPaxResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ConfirmOrderMapperTest {
    ConfirmOrderMapper confirmOrderMapper = Mappers.getMapper(ConfirmOrderMapper.class);

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
        ConnectorConfirmOrderRequest mappedConfirmOrderResponse = confirmOrderMapper
                .orderEntityToConnectorConfirmOrderRequest(order);

        connectorConfirmOrderRequest.setExpirationDate(date);
        connectorConfirmOrderRequest.setSubmittedDate(date);
        mappedConfirmOrderResponse.setSubmittedDate(date);
        mappedConfirmOrderResponse.setExpirationDate(date);

        assertEquals(connectorConfirmOrderRequest, mappedConfirmOrderResponse);
    }

    @Test
    void shouldMapConnectorConfirmOrderStatusResponseToStatusEntity() {
        LocalDateTime date = LocalDateTime.now();
        ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse = MockBuilder
                .connectorConfirmOrderStatusResponse();
        OrderStatusEntity mappedOrderStatusEntity = confirmOrderMapper
                .connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderStatusResponse);
        OrderStatusEntity orderStatusEntity = MockBuilder.statusInitial();

        orderStatusEntity.setStatusDate(date);
        mappedOrderStatusEntity.setStatusDate(date);

        assertEquals(orderStatusEntity, mappedOrderStatusEntity);
    }

    @Test
    void shouldMapPaxEntityToConnectorConfirmOrderPaxRequest() {
    ConnectorConfirmOrderPaxRequest mappedConnectorConfirmOrderPaxRequest = confirmOrderMapper.paxEntityToConnectorConfirmOrderPaxRequest(MockBuilder.paxEntity());
    assertInstanceOf(ConnectorConfirmOrderPaxRequest.class,
    mappedConnectorConfirmOrderPaxRequest);
    }

    @Test
    void shouldOrderEntityToConnectorConfirmOrderRequest() {
    ConnectorConfirmOrderRequest mappedConnectorConfirmOrderRequest = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(MockBuilder.orderEntity());
    assertInstanceOf(ConnectorConfirmOrderRequest.class,
    mappedConnectorConfirmOrderRequest);
    }

    @Test
    void shouldPaxEntityToConfirmationOrderPaxResponse() {
        ConfirmationOrderPaxResponse mappedPaxEntityToConfirmationOrderPaxResponse = confirmOrderMapper.paxEntityToConfirmationOrderPaxResponse(MockBuilder.paxEntity());
        assertInstanceOf(ConfirmationOrderPaxResponse.class, mappedPaxEntityToConfirmationOrderPaxResponse);
    }

    @Test
    void shouldConfirmationOrderDocumentResponse() {
        ConfirmationOrderDocumentResponse mappedPaxEntityToConfirmationOrderDocumentResponse = confirmOrderMapper.paxEntityToConfirmationOrderPaxResponse(MockBuilder.documentEntity());
        assertInstanceOf(ConfirmationOrderDocumentResponse.class, mappedPaxEntityToConfirmationOrderDocumentResponse);
    }

    @Test
    void shouldFlightLegEntityToConfirmationOrderFlightsLegsResponse() {
        ConfirmationOrderFlightsLegsResponse mappedConfirmationOrderFlightsLegsResponse = confirmOrderMapper.flightLegEntityToConfirmationOrderFlightsLegsResponse(MockBuilder.flightLegEntity());
        assertInstanceOf(ConfirmationOrderFlightsLegsResponse.class, mappedConfirmationOrderFlightsLegsResponse);
    }
}