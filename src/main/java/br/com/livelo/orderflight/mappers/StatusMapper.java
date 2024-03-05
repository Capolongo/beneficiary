package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    @Mapping(target = "description", source = "status.message")
    @Mapping(target = "code", source = "status.code")
    @Mapping(target = "partnerCode", source = "status.code")
    @Mapping(target = "partnerDescription", source = "status.message")
    @Mapping(target = "partnerResponse", source = "oldStatus.partnerResponse")
    @Mapping(target = "statusDate", source = "oldStatus.statusDate")
    OrderStatusEntity convert(UpdateStatusDTO status, OrderStatusEntity oldStatus);

}
