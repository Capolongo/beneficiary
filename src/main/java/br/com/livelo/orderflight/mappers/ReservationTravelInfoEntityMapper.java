package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Objects;


@Mapper(componentModel = "spring")

public interface ReservationTravelInfoEntityMapper {

    @Mapping(target = "paxs", source = "reservationRequest.paxs")
    TravelInfoEntity toReservationTravelInfoEntity(ReservationRequest reservationRequest, PartnerReservationTravelInfo partnerReservationTravelInfo);

    @AfterMapping
    default void setType(PartnerReservationTravelInfo partnerReservationTravelInfo, @MappingTarget TravelInfoEntity travelInfoEntity) {
        travelInfoEntity.setType(Objects.nonNull(partnerReservationTravelInfo.getType()) ? partnerReservationTravelInfo.getType() : "");
    }

}
