package br.com.livelo.orderflight.mappers;


import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationStatusMapper {

    @Mapping(target = "code", source = "partnerReservationResponse.status")
    OrderStatusEntity toOrderStatus(PartnerReservationResponse partnerReservationResponse);
}