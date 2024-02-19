package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@Mapper(componentModel = "spring", uses = ReservationOrderPriceDescriptionMapper.class)
public interface ReservationPriceMapper {

    @Mapping(target = "priceListId", source = "listPrice")
    @Mapping(target = "partnerAmount", source = "partnerReservationResponse.amount")
    @Mapping(target = "accrualPoints", source = "pricingCalculatePrice.accrualPoints")
    @Mapping(target = "amount", source = "pricingCalculatePrice.amount")
    @Mapping(target = "pointsAmount", source = "pricingCalculatePrice.pointsAmount")
    @Mapping(target = "ordersPriceDescription", expression = "java(mapOrdersPriceDescription(partnerReservationResponse,pricingCalculatePrice))")
    OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse, String listPrice, PricingCalculatePrice pricingCalculatePrice);

    default Set<OrderPriceDescriptionEntity> mapOrdersPriceDescription(PartnerReservationResponse partnerReservationResponse, PricingCalculatePrice pricingCalculatePrice) {
        var orderPriceDescriptionMapper = Mappers.getMapper(ReservationOrderPriceDescriptionMapper.class);
        var orderPriceDescriptionTaxesMapper = Mappers.getMapper(ReservationOrderPriceDescriptionTaxesMapper.class);

        if (partnerReservationResponse.getOrdersPriceDescription() != null) {
            Set<OrderPriceDescriptionEntity> ordersPriceDescription = new HashSet<>();

            for (PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription : partnerReservationResponse.getOrdersPriceDescription()) {
                OrderPriceDescriptionEntity orderPriceDescriptionEntity = orderPriceDescriptionMapper.toOrderPriceDescriptionEntity(partnerReservationOrdersPriceDescription);

                var pricingCalculatePricesDescription = pricingCalculatePrice.getPricesDescription()
                        .stream()
                        .filter(priceDescription -> partnerReservationOrdersPriceDescription.getType().equals(priceDescription.getPassengerType()))
                        .findFirst()
                        .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Passenger type not found"));

                orderPriceDescriptionEntity.setPointsAmount(BigDecimal.valueOf(pricingCalculatePricesDescription.getPointsAmount()));
                ordersPriceDescription.add(orderPriceDescriptionEntity);

                for (PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes : partnerReservationOrdersPriceDescription.getTaxes()) {
                    OrderPriceDescriptionEntity orderPriceDescriptionEntityTaxes = orderPriceDescriptionTaxesMapper.toOrderPriceDescriptionEntity(partnerReservationOrdersPriceDescriptionTaxes);

                    PricingCalculateTaxes pricingCalculateTaxes = pricingCalculatePricesDescription.getTaxes()
                            .stream().filter(priceDescriptionTaxes -> orderPriceDescriptionEntityTaxes.getDescription().equals(priceDescriptionTaxes.getDescription()))
                            .findFirst()
                            .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Tax description not found"));

                    orderPriceDescriptionEntityTaxes.setPointsAmount(BigDecimal.valueOf(pricingCalculateTaxes.getPointsAmount()));
                    ordersPriceDescription.add(orderPriceDescriptionEntityTaxes);
                }
            }
            return ordersPriceDescription;
        }
        return Collections.emptySet();
    }
}
