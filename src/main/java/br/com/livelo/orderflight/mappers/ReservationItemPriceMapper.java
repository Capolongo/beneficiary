package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ReservationItemPriceMapper {
    String FLIGHT_TYPE = "type_flight";
    String TAX_TYPE = "type_flight_tax";
    @Mapping(target = "partnerAmount", source = "partnerReservationItem.amount")
    @Mapping(target = "amount", expression = "java(amount(partnerReservationItem,pricingCalculatePrice))")
    @Mapping(target = "pointsAmount", expression = "java(pointsAmount(partnerReservationItem,pricingCalculatePrice))")
    @Mapping(target = "accrualPoints", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "priceListId", source = "priceList")
    @Mapping(target = "listPrice", ignore = true)
    @Mapping(target = "priceRule", ignore = true)
    OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem, String priceList, PricingCalculatePrice pricingCalculatePrice);

    default BigDecimal pointsAmount(PartnerReservationItem partnerReservationItem, PricingCalculatePrice pricingCalculatePrice) {
        if (FLIGHT_TYPE.equals(partnerReservationItem.getType())) {
            return pricingCalculatePrice.getFlight().getPointsAmount();
        }
        if (TAX_TYPE.equals(partnerReservationItem.getType())) {
            return pricingCalculatePrice.getTaxes().getPointsAmount();
        }
        return BigDecimal.ZERO;
    }

    default BigDecimal amount(PartnerReservationItem partnerReservationItem, PricingCalculatePrice pricingCalculatePrice) {
        if (FLIGHT_TYPE.equals(partnerReservationItem.getType())) {
            return pricingCalculatePrice.getFlight().getAmount();
        }
        if (TAX_TYPE.equals(partnerReservationItem.getType())) {
            return pricingCalculatePrice.getTaxes().getAmount();
        }
        return BigDecimal.ZERO;
    }
}
