package br.com.livelo.orderflight.mapper;

import br.com.livelo.orderflight.domain.dtos.response.ConfirmResponseDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfirmOrderMapper {
//    @Mapping(source = "currentStatus.code", target = "status.code")
    @Mapping(source = "currentStatus.partnerDescription", target = "status.details")
     ConfirmResponseDTO entityToResponseDTO(OrderEntity orderEntity);
}
