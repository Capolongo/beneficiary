package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")

public interface ReservationTravelInfoEntityMapper {

    @Mapping(target = "type", source = "reservationRequest.partnerOrderId") //todo ver se o type vem do connectas
    @Mapping(target = "paxs", source = "reservationRequest.paxs")
    TravelInfoEntity toReservationTravelInfoEntity(ReservationRequest reservationRequest);

}
