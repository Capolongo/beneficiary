package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderDocumentResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderFlightsLegsResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderPaxResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.PartnerConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.PartnerConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
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
        PartnerConfirmOrderRequest connectorConfirmOrderRequest = MockBuilder.connectorConfirmOrderRequest();
        PartnerConfirmOrderRequest mappedConfirmOrderResponse = confirmOrderMapper
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
        PartnerConfirmOrderStatusResponse connectorConfirmOrderStatusResponse = MockBuilder
                .connectorConfirmOrderStatusResponse();
        OrderCurrentStatusEntity mappedOrderStatusHistoryEntity = confirmOrderMapper
                .connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderStatusResponse);
        OrderCurrentStatusEntity orderStatusHistoryEntity = MockBuilder.statusInitial();

        assertEquals(orderStatusHistoryEntity, mappedOrderStatusHistoryEntity);
    }

    @Test
    void shouldMapPaxEntityToConnectorConfirmOrderPaxRequest() {
    PartnerConfirmOrderPaxRequest mappedConnectorConfirmOrderPaxRequest = confirmOrderMapper.paxEntityToConnectorConfirmOrderPaxRequest(MockBuilder.paxEntity());
    assertInstanceOf(PartnerConfirmOrderPaxRequest.class,
    mappedConnectorConfirmOrderPaxRequest);
    }

    @Test
    void shouldOrderEntityToConnectorConfirmOrderRequest() {
    PartnerConfirmOrderRequest mappedConnectorConfirmOrderRequest = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(MockBuilder.orderEntity());
    assertInstanceOf(PartnerConfirmOrderRequest.class,
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