package br.com.livelo.orderflight.mapper;

import org.mapstruct.Mapper;

import br.com.livelo.orderflight.domain.dtos.connector.ConnectorRequestDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

@Mapper(componentModel = "spring")
public interface ConnectorMapper {
  ConnectorRequestDTO toConnectorRequestDTO(OrderEntity orderEntity);
}