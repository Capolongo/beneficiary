package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationPax;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationPaxMap {
    @Mapping(target = "phoneNumber", source = "reservationPax.phone")
    @Mapping(target = "document", source = "reservationPax.documents")
    PaxEntity toPaxEntity(ReservationPax reservationPax);
}
