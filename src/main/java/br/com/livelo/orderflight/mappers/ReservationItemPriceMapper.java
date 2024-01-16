package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReservationItemPriceMapper {

    @Mapping(target = "listPrice", source = "amount")
    OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem);
}

