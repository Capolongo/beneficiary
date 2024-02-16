package br.com.livelo.orderflight.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionTaxesMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pointsAmount", ignore = true)
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes);
}
