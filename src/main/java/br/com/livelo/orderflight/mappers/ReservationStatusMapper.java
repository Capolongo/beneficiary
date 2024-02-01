package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationStatusMapper {

    @Mapping(target = "code", source = "partnerReservationResponse.status")
    @Mapping(target = "statusDate", expression = "java(java.time.LocalDateTime.now())")
    OrderStatusEntity toOrderStatus(PartnerReservationResponse partnerReservationResponse);
}