package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationFlightLegMapper {
    FlightLegEntity toFlightLegEntity(PartnerReservationFlightsLeg partnerReservationFlightsLeg);
}
