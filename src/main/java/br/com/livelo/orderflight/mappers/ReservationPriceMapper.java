package br.com.livelo.orderflight.mappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;

@Mapper(componentModel = "spring", uses = ReservationOrderPriceDescriptionMapper.class)
public interface ReservationPriceMapper {

    @Mapping(target = "priceListId", source = "listPrice")
    @Mapping(target = "partnerAmount", source = "partnerReservationResponse.amount")
    @Mapping(target = "accrualPoints", source = "pricingCalculatePrice.accrualPoints")
    @Mapping(target = "amount", source = "pricingCalculatePrice.amount")
    @Mapping(target = "pointsAmount", source = "pricingCalculatePrice.pointsAmount")
    @Mapping(target = "ordersPriceDescription", expression = "java(mapOrdersPriceDescription(partnerReservationResponse,pricingCalculatePrice))")
    OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse, String listPrice, PricingCalculatePrice pricingCalculatePrice);

    default Set<OrderPriceDescriptionEntity> mapOrdersPriceDescription(
            PartnerReservationResponse partnerReservationResponse,PricingCalculatePrice pricingCalculatePrice) {
        
    	var orderPriceDescriptionMapper = Mappers.getMapper(ReservationOrderPriceDescriptionMapper.class);
        var orderPriceDescriptionTaxesMapper = Mappers.getMapper(ReservationOrderPriceDescriptionTaxesMapper.class);
        
        if (partnerReservationResponse.getOrdersPriceDescription() != null) {
        	Set<OrderPriceDescriptionEntity> ordersPriceDescription = new HashSet<>();
        	
        	for(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription:partnerReservationResponse.getOrdersPriceDescription()) {
        		ordersPriceDescription.add(orderPriceDescriptionMapper.toOrderPriceDescriptionEntity(partnerReservationOrdersPriceDescription));
        		
        		for(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes:partnerReservationOrdersPriceDescription.getTaxes()) {
        			ordersPriceDescription.add(orderPriceDescriptionTaxesMapper.toOrderPriceDescriptionEntity(partnerReservationOrdersPriceDescriptionTaxes));
        		}
        	}
            return ordersPriceDescription;
        }
        return Collections.emptySet();
    }
}
