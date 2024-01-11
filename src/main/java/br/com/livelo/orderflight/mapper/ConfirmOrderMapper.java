package br.com.livelo.orderflight.mapper;

import br.com.livelo.orderflight.domain.dtos.connector.ConnectorRequestDTO;
import br.com.livelo.orderflight.domain.dtos.connector.PaxsConnectorRequestDTO;
import br.com.livelo.orderflight.domain.dtos.response.ConfirmResponseDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

import br.com.livelo.orderflight.domain.entity.PaxEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfirmOrderMapper {
    @Mapping(source = "currentStatus.partnerDescription", target = "status.details")
    ConfirmResponseDTO entityToResponseDTO(OrderEntity orderEntity);

    @Mapping(target = "commerceItemId", expression = "java(getFirstItemCommerceItemId(orderEntity))")
    @Mapping(target = "paxs", expression = "java(reducePaxs(orderEntity))")
    ConnectorRequestDTO orderEntityToConnectorRequestDTO(OrderEntity orderEntity);

    @Mapping(target = "phone", source = "phoneNumber")
    PaxsConnectorRequestDTO paxEntityToPaxsConnectorRequestDTO(PaxEntity pax);

    default String getFirstItemCommerceItemId(OrderEntity orderEntity) {
        return orderEntity.getItems().stream().filter(item -> item.getSkuId().equals("cvc_flight")).findFirst()
                .get().getCommerceItemId();
    }

    default List<PaxsConnectorRequestDTO> reducePaxs(OrderEntity orderEntity) {
        return orderEntity.getItems().stream()
                .filter(item -> item.getSkuId().equals("cvc_flight"))
                .findFirst()
                .get()
                .getTravelInfo()
                .getPaxs()
                .stream().map(this::paxEntityToPaxsConnectorRequestDTO)
                .toList();
    }
}
