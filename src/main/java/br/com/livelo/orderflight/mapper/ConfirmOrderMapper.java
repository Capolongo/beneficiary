package br.com.livelo.orderflight.mapper;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfirmOrderMapper {
    @Mapping(source = "currentStatus.partnerDescription", target = "status.details")
    ConfirmOrderResponse orderEntityToConfirmOrderResponse(OrderEntity orderEntity);

    @Mapping(target = "commerceItemId", expression = "java(getFlightItemCommerceItemId(orderEntity))")
    @Mapping(target = "paxs", expression = "java(reducePaxs(orderEntity))")
    ConnectorConfirmOrderRequest orderEntityToConnectorConfirmOrderRequest(OrderEntity orderEntity);

    @Mapping(target = "phone", source = "phoneNumber")
    ConnectorConfirmOrderPaxRequest paxEntityToConnectorConfirmOrderPaxRequest(PaxEntity pax);

    OrderStatusEntity ConnectorConfirmOrderStatusResponseToStatusEntity(ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse);

    default String getFlightItemCommerceItemId(OrderEntity orderEntity) {
        return orderEntity.getItems().stream().filter(item -> !item.getSkuId().toUpperCase().contains("TAX")).findFirst()
                .get().getCommerceItemId();
    }

    default List<ConnectorConfirmOrderPaxRequest> reducePaxs(OrderEntity orderEntity) {
        return orderEntity.getItems().stream()
                .filter(item -> !item.getSkuId().toUpperCase().contains("TAX"))
                .findFirst()
                .get()
                .getTravelInfo()
                .getPaxs()
                .stream().map(this::paxEntityToConnectorConfirmOrderPaxRequest)
                .toList();
    }
}
