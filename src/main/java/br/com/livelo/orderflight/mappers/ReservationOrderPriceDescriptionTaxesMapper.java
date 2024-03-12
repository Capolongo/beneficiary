package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionTaxesMapper {
    @Mapping(target = "amount", source = "partnerReservationOrdersPriceDescriptionTaxes.amount")
    @Mapping(target = "type", source = "partnerReservationOrdersPriceDescriptionTaxes.type")
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes);
}
