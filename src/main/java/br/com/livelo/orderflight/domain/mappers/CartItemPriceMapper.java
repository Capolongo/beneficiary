package br.com.livelo.orderflight.domain.mappers;

import br.com.livelo.orderflight.domain.dto.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartItemPriceMapper {

    @Mapping(target = "listPrice", source = "amount")
    OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem);
}

