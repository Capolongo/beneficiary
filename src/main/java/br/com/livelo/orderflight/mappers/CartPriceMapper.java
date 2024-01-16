package br.com.livelo.orderflight.mappers;


import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartPriceMapper {

    @Mapping(target = "priceListId", ignore = true)
    OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse);
}
