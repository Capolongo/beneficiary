package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    @Mapping(target = "description", source = "status.message")
    @Mapping(target = "code", source = "status.code")
    @Mapping(target = "partnerCode", source = "status.code")
    OrderCurrentStatusEntity convert(UpdateStatusDTO status);

}
