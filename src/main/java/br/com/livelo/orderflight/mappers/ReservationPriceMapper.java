package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", uses = ReservationOrderPriceDescriptionMapper.class)
public interface ReservationPriceMapper {

    @Mapping(target = "priceListId", source = "listPrice")
    @Mapping(target = "partnerAmount", source = "partnerReservationResponse.amount")
    @Mapping(target = "ordersPriceDescription", expression = "java(mapOrdersPriceDescription(partnerReservationResponse))")
    OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse, String listPrice);

    default Set<OrderPriceDescriptionEntity> mapOrdersPriceDescription(PartnerReservationResponse partnerReservationResponse) {
        var orderPriceDescriptionMapper = Mappers.getMapper(ReservationOrderPriceDescriptionMapper.class);
        var orderPriceDescriptionTaxesMapper = Mappers.getMapper(ReservationOrderPriceDescriptionTaxesMapper.class);

        if (partnerReservationResponse.getOrdersPriceDescription() != null) {
            Set<OrderPriceDescriptionEntity> ordersPriceDescription = new HashSet<>();

            for (PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight : partnerReservationResponse.getOrdersPriceDescription().getFlights()) {
                ordersPriceDescription.add(orderPriceDescriptionMapper.toOrderPriceDescriptionEntity(
                        partnerReservationOrdersPriceDescriptionFlight));
            }

            for (PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes : partnerReservationResponse.getOrdersPriceDescription().getTaxes()) {
                ordersPriceDescription.add(orderPriceDescriptionTaxesMapper.toOrderPriceDescriptionEntity(
                        partnerReservationOrdersPriceDescriptionTaxes));
            }
            return ordersPriceDescription;
        }
        return Collections.emptySet();
    }
}
