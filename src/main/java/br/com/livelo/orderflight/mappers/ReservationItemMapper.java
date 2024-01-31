package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ReservationItemPriceMapper.class)
public interface ReservationItemMapper {
    @Mapping(target = "commerceItemId", source = "reservationItem.commerceItemId")
    @Mapping(target = "productId", source = "reservationItem.productId")
    @Mapping(target = "quantity", source = "partnerReservationItem.quantity")
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationItem, listPrice))")
    OrderItemEntity toOrderItemEntity(ReservationItem reservationItem, PartnerReservationItem partnerReservationItem, String listPrice);

    default OrderItemPriceEntity mapPrice(PartnerReservationItem partnerReservationItem, String listPrice) {
        var reservationItemPriceMapper = Mappers.getMapper(ReservationItemPriceMapper.class);
        return reservationItemPriceMapper.toOrderItemPriceEntity(partnerReservationItem, listPrice);
    }


}