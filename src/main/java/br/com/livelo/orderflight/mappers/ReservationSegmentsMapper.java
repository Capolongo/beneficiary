package br.com.livelo.orderflight.mappers;


import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
import br.com.livelo.orderflight.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ReservationLuggagesMapper.class, ReservationCancelationRulesMapper.class, ReservationChangeRulesMapper.class, ReservationFlightLegMapper.class})


public interface ReservationSegmentsMapper {
    @Mapping(target = "luggages", expression = "java(mapLuggages(partnerReservationSegment))")
    @Mapping(target = "cancelationRules", expression = "java(mapCancelationRules(partnerReservationSegment))")
    @Mapping(target = "changeRules", expression = "java(mapChangeRules(partnerReservationSegment))")
    @Mapping(target = "flightsLegs", expression = "java(mapFlightLeg(partnerReservationSegment))")
    SegmentEntity toSegmentEntity(PartnerReservationSegment partnerReservationSegment);

    default Set<LuggageEntity> mapLuggages(PartnerReservationSegment partnerReservationSegment) {
        var luggageMapper = Mappers.getMapper(ReservationLuggagesMapper.class);

        return partnerReservationSegment.getLuggages()
                .stream()
                .map(luggageMapper::toReservationLuggageEntity)
                .collect(Collectors.toSet());
    }

    default Set<CancelationRuleEntity> mapCancelationRules(PartnerReservationSegment partnerReservationSegment) {
        var cancelationRulesMapper = Mappers.getMapper(ReservationCancelationRulesMapper.class);

        return partnerReservationSegment.getCancelationRules()
                .stream()
                .map(cancelationRulesMapper::toCancelationRuleEntity)
                .collect(Collectors.toSet());
    }

    default Set<ChangeRuleEntity> mapChangeRules(PartnerReservationSegment partnerReservationSegment) {
        var changeRulesMapper = Mappers.getMapper(ReservationChangeRulesMapper.class);

        return partnerReservationSegment.getChangeRules()
                .stream()
                .map(changeRulesMapper::toChangeRuleEntity)
                .collect(Collectors.toSet());
    }

    default Set<FlightLegEntity> mapFlightLeg(PartnerReservationSegment partnerReservationSegment) {
        var flightLegMapper = Mappers.getMapper(ReservationFlightLegMapper.class);

        return partnerReservationSegment.getFlightLegs()
                .stream()
                .map(flightLegMapper::toFlightLegEntity)
                .collect(Collectors.toSet());
    }
}

