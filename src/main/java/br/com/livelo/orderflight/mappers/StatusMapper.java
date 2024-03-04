package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    @Mapping(source = "message", target = "description")
    OrderStatusEntity convert(UpdateStatusDTO statusDTO);

}
