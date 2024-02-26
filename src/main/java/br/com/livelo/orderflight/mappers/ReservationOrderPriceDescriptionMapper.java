package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ReservationOrderPriceDescriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pointsAmount", source = "pointsAmount")
    @Mapping(target = "amount", source = "partnerReservationOrdersPriceDescriptionFlight.amount")
    @Mapping(target = "type", source = "partnerReservationOrdersPriceDescriptionFlight.passengerType")
    OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight, BigDecimal pointsAmount);
}
