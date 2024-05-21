package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.*;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmDocumentRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfirmOrderMapper {
    @Mapping(target = "productType", source = "productId")
    ConfirmOrderItemResponse orderItemEntityToConfirmOrderItemResponse(OrderItemEntity orderItemEntity);

    @Mapping(target = "operatedBy", source = "flightLegEntity.airlineOperatedByIata")
    @Mapping(target = "airline", ignore = true)
    ConfirmationOrderFlightsLegsResponse flightLegEntityToConfirmationOrderFlightsLegsResponse(FlightLegEntity flightLegEntity);

    ConfirmationOrderDocumentResponse paxEntityToConfirmationOrderPaxResponse(DocumentEntity documentEntity);

    @Mapping(target = "phone", source = "phoneNumber")
    ConfirmationOrderPaxResponse paxEntityToConfirmationOrderPaxResponse(PaxEntity paxEntity);

    @Mapping(source = "currentStatus.partnerResponse", target = "status.details")
    ConfirmOrderResponse orderEntityToConfirmOrderResponse(OrderEntity orderEntity);

    @Mapping(target = "commerceItemId", expression = "java(getFlightItemCommerceItemId(orderEntity))")
    @Mapping(target = "partnerOrderLinkId", expression = "java(getFlightItemPartnerOrderLinkId(orderEntity))")
    @Mapping(target = "paxs", expression = "java(reducePaxs(orderEntity))")
    ConnectorConfirmOrderRequest orderEntityToConnectorConfirmOrderRequest(OrderEntity orderEntity);

    ConnectorConfirmOrderPaxRequest paxEntityToConnectorConfirmOrderPaxRequest(PaxEntity pax);

    @Mapping(target = "number", source = "documentNumber")
    ConnectorConfirmDocumentRequest documentEntityToConnectorConfirmDocumentRequest(DocumentEntity document);

    OrderStatusEntity connectorConfirmOrderStatusResponseToStatusEntity(ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse);

    default String getFlightItemPartnerOrderLinkId(OrderEntity orderEntity) {
        return orderEntity.getItems().stream()
                .filter(item -> !item.getSkuId().toUpperCase().contains("TAX"))
                .findFirst()
                .map(OrderItemEntity::getPartnerOrderLinkId)
                .orElse("");
    }

    default String getFlightItemCommerceItemId(OrderEntity orderEntity) {
        return orderEntity.getItems().stream()
                .filter(item -> !item.getSkuId().toUpperCase().contains("TAX"))
                .findFirst()
                .map(OrderItemEntity::getCommerceItemId)
                .orElse("");
    }

    default List<ConnectorConfirmOrderPaxRequest> reducePaxs(OrderEntity orderEntity) {
        return orderEntity.getItems().stream()
                .filter(item -> !item.getSkuId().toUpperCase().contains("TAX"))
                .findFirst()
                .map(item -> item.getTravelInfo().getPaxs().stream()
                        .map(this::paxEntityToConnectorConfirmOrderPaxRequest)
                        .toList())
                .orElse(Collections.emptyList());
    }
}
