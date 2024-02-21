package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;

import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pointsAmount", source = "pointsAmount")
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription, BigDecimal pointsAmount);
}
