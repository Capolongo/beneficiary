package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ReservationItemPriceMapper {

    @Mapping(target = "partnerAmount", source = "partnerReservationItem.amount")
    @Mapping(target = "amount", expression = "java(amount(pricingCalculateFlight,pricingCalculateTaxes))")
    @Mapping(target = "pointsAmount", expression = "java(pointsAmount(pricingCalculateFlight,pricingCalculateTaxes))")
    @Mapping(target = "accrualPoints", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "priceListId", source = "priceList")
    @Mapping(target = "listPrice", ignore = true)
    @Mapping(target = "priceRule", ignore = true)
    OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem, String priceList, PricingCalculateFlight pricingCalculateFlight, PricingCalculateTaxes pricingCalculateTaxes);

    default BigDecimal pointsAmount(PricingCalculateFlight pricingCalculateFlight, PricingCalculateTaxes pricingCalculateTaxes) {
       if(Objects.nonNull(pricingCalculateFlight)){
           return pricingCalculateFlight.getPointsAmount();
       }
       if(Objects.nonNull(pricingCalculateTaxes)){
           return pricingCalculateTaxes.getPointsAmount();
       }
       return BigDecimal.ZERO;
    }

    default BigDecimal amount(PricingCalculateFlight pricingCalculateFlight, PricingCalculateTaxes pricingCalculateTaxes) {
        if(Objects.nonNull(pricingCalculateFlight)){
            return pricingCalculateFlight.getAmount();
        }
        if(Objects.nonNull(pricingCalculateTaxes)){
            return pricingCalculateTaxes.getAmount();
        }
        return BigDecimal.ZERO;
    }
}
