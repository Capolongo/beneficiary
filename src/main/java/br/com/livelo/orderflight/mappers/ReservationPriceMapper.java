package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ReservationOrderPriceDescriptionMapper.class)
public interface ReservationPriceMapper {

    @Mapping(target = "priceListId", source = "listPrice")
    @Mapping(target = "partnerAmount", source = "partnerReservationResponse.amount")
    @Mapping(target = "accrualPoints", constant = "10.0")
    @Mapping(target = "amount", expression = "java(java.math.BigDecimal.TEN)")
    @Mapping(target = "pointsAmount", expression = "java(java.math.BigDecimal.TEN)")
    @Mapping(target = "ordersPriceDescription", expression = "java(mapOrdersPriceDescription(partnerReservationResponse))")
    OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse, String listPrice);

    default Set<OrderPriceDescriptionEntity> mapOrdersPriceDescription(
            PartnerReservationResponse partnerReservationResponse) {
        var orderPriceDescriptionMapper = Mappers.getMapper(ReservationOrderPriceDescriptionMapper.class);

        if (partnerReservationResponse.getOrdersPriceDescription() != null) {
            return partnerReservationResponse.getOrdersPriceDescription()
                    .stream()
                    .map(orderPriceDescriptionMapper::toOrderPriceDescriptionEntity)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
