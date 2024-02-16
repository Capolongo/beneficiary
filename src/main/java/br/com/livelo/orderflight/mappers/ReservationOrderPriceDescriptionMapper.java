package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;

import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pointsAmount", ignore = true)
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription);
}
