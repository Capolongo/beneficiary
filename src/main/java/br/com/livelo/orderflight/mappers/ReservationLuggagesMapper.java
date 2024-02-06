package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationLuggage;
import br.com.livelo.orderflight.domain.entity.LuggageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationLuggagesMapper {

    LuggageEntity toReservationLuggageEntity(PartnerReservationLuggage partnerReservationLuggage);
}
