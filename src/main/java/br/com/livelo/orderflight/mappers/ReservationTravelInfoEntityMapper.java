package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")

public interface ReservationTravelInfoEntityMapper {

    @Mapping(target = "paxs", source = "reservationRequest.paxs")
    @Mapping(target = "type", source = "partnerReservationTravelInfo.type")
    TravelInfoEntity toReservationTravelInfoEntity(ReservationRequest reservationRequest, PartnerReservationTravelInfo partnerReservationTravelInfo);

}
