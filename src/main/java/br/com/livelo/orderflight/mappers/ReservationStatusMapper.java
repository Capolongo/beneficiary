package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusHistoryEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReservationStatusMapper {

    @Mapping(target = "code", source = "partnerReservationResponse.currentStatus.code")
    @Mapping(target = "description", source = "partnerReservationResponse.currentStatus.description")
    @Mapping(target = "partnerCode", source = "partnerReservationResponse.currentStatus.partnerCode")
    @Mapping(target = "partnerDescription", source = "partnerReservationResponse.currentStatus.partnerDescription")
    OrderCurrentStatusEntity toOrderStatus(PartnerReservationResponse partnerReservationResponse);

    @Mapping(target = "code", source = "partnerReservationResponse.currentStatus.code")
    @Mapping(target = "description", source = "partnerReservationResponse.currentStatus.description")
    @Mapping(target = "partnerCode", source = "partnerReservationResponse.currentStatus.partnerCode")
    @Mapping(target = "partnerDescription", source = "partnerReservationResponse.currentStatus.partnerDescription")
    OrderStatusHistoryEntity toOrderStatusHistory(PartnerReservationResponse partnerReservationResponse);



    @AfterMapping
    default void buildPartnerResponse(PartnerReservationResponse partnerReservationResponse, @MappingTarget OrderStatusHistoryEntity orderStatusHistoryEntity) throws JsonProcessingException {
        orderStatusHistoryEntity.setPartnerResponse(new ObjectMapper().writeValueAsString(partnerReservationResponse));
    }

}