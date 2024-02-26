package br.com.livelo.orderflight.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionTaxesMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pointsAmount", source = "pointsAmount")
    @Mapping(target = "amount", source = "partnerReservationOrdersPriceDescriptionTaxes.amount")
    @Mapping(target = "type", source = "partnerReservationOrdersPriceDescriptionTaxes.type")
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes, BigDecimal pointsAmount);
}
