package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
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

            for (PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight : partnerReservationResponse.getOrdersPriceDescription().getFlights()) {
                ordersPriceDescription.add(orderPriceDescriptionMapper.toOrderPriceDescriptionEntity(
                        partnerReservationOrdersPriceDescriptionFlight,
                        getPointsAmountFromPricingCalculate(partnerReservationOrdersPriceDescriptionFlight, pricingCalculatePrice)));
            }

            for (PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes : partnerReservationResponse.getOrdersPriceDescription().getTaxes()) {
                ordersPriceDescription.add(orderPriceDescriptionTaxesMapper.toOrderPriceDescriptionEntity(
                        partnerReservationOrdersPriceDescriptionTaxes,
                        getPointsAmountFromPricingCalculateTaxes(partnerReservationOrdersPriceDescriptionTaxes, pricingCalculatePrice)));
            }
            return ordersPriceDescription;
        }
        return Collections.emptySet();
    }

    default BigDecimal getPointsAmountFromPricingCalculateTaxes(PartnerReservationOrdersPriceDescriptionTaxes orderPriceDescriptionEntityTaxes, PricingCalculatePrice pricingCalculatePrice) {
        return pricingCalculatePrice.getPricesDescription().getTaxes().stream()
                .filter(priceDescriptionTaxes -> orderPriceDescriptionEntityTaxes.getDescription().equals(priceDescriptionTaxes.getType()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Tax description not found")).getPointsAmount();
    }

    default BigDecimal getPointsAmountFromPricingCalculate(PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight, PricingCalculatePrice pricingCalculatePrice) {
        return pricingCalculatePrice.getPricesDescription().getFlights().stream()
                .filter(priceDescription -> partnerReservationOrdersPriceDescriptionFlight.getPassengerType().equals(priceDescription.getPassengerType()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Passenger type not found")).getPointsAmount();
    }
}
