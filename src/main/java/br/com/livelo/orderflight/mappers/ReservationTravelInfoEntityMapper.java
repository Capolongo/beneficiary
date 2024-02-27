package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationPax;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {ReservationPaxMap.class})

public interface ReservationTravelInfoEntityMapper {

    @Mapping(target = "paxs", expression = "java(mapPax(reservationRequest))")
    TravelInfoEntity toReservationTravelInfoEntity(ReservationRequest reservationRequest, PartnerReservationTravelInfo partnerReservationTravelInfo);


    default Set<PaxEntity> mapPax(ReservationRequest reservationRequest) {
        var mapperPax = Mappers.getMapper(ReservationPaxMap.class);

        List<ReservationPax> paxs = reservationRequest.getPaxs();

        return paxs.stream().map(mapperPax::toPaxEntity).collect(Collectors.toSet());
    }

    @AfterMapping
    default void setType(PartnerReservationTravelInfo partnerReservationTravelInfo, @MappingTarget TravelInfoEntity travelInfoEntity) {
        travelInfoEntity.setType(Objects.nonNull(partnerReservationTravelInfo.getType()) ? partnerReservationTravelInfo.getType() : "");
    }


}
