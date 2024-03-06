package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface LiveloPartnersMapper {
    UpdateOrderDTO orderEntityToUpdateOrderDTO(OrderEntity order);
}
