package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationItemPriceMapper {

    @Mapping(target = "partnerAmount", source = "partnerReservationItem.amount")
    @Mapping(target = "accrualPoints", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "priceListId", source = "priceList")
    @Mapping(target = "listPrice", ignore = true)
    @Mapping(target = "priceRule", ignore = true)
    OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem, String priceList);
}
