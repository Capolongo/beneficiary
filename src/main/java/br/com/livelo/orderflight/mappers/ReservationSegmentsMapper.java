package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationSegmentsMapper {
    SegmentEntity toSegmentEntity(PartnerReservationSegment partnerReservationSegment);
}
