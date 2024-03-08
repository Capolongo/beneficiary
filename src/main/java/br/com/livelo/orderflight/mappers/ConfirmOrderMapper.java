package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderItemResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderDocumentResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderFlightsLegsResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderPaxResponse;
import br.com.livelo.orderflight.domain.entity.DocumentEntity;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfirmOrderMapper {
    @Mapping(target = "productType", source = "productId")
    ConfirmOrderItemResponse orderItemEntityToConfirmOrderItemResponse(OrderItemEntity orderItemEntity);

    @Mapping(target = "operatedBy", source = "managedBy")
    @Mapping(target = "airline", ignore = true)
    ConfirmationOrderFlightsLegsResponse flightLegEntityToConfirmationOrderFlightsLegsResponse(FlightLegEntity flightLegEntity);

    ConfirmationOrderDocumentResponse paxEntityToConfirmationOrderPaxResponse(DocumentEntity documentEntity);

    @Mapping(target = "phone", source = "phone")
    ConfirmationOrderPaxResponse paxEntityToConfirmationOrderPaxResponse(PaxEntity paxEntity);

    @Mapping(source = "currentStatus.partnerResponse", target = "status.details")
    ConfirmOrderResponse orderEntityToConfirmOrderResponse(OrderEntity orderEntity);

    @Mapping(target = "commerceItemId", expression = "java(getFlightItemCommerceItemId(orderEntity))")
    @Mapping(target = "paxs", expression = "java(reducePaxs(orderEntity))")
    ConnectorConfirmOrderRequest orderEntityToConnectorConfirmOrderRequest(OrderEntity orderEntity);

    @Mapping(target = "phone", source = "phone")
    ConnectorConfirmOrderPaxRequest paxEntityToConnectorConfirmOrderPaxRequest(PaxEntity pax);

    OrderStatusEntity connectorConfirmOrderStatusResponseToStatusEntity(ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse);

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
