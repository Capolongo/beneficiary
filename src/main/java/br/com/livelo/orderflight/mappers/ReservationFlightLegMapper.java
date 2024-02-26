package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationFlightLegMapper {
    @Mapping(target = "flightNumber", source = "flightNumber")
    @Mapping(target = "flightDuration", source = "flightDuration")
    @Mapping(target = "airline", source = "operatedBy")
    @Mapping(target = "managedBy", source = "managedBy")
    @Mapping(target = "timeToWait", source = "timeToWait")
    @Mapping(target = "originIata", source = "originIata")
    @Mapping(target = "originDescription", source = "originDescription")
    @Mapping(target = "destinationIata", source = "destinationIata")
    @Mapping(target = "destinationDescription", source = "destinationDescription")
    @Mapping(target = "departureDate", source = "departureDate")
    @Mapping(target = "arrivalDate", source = "arrivalDate")
    @Mapping(target = "type", source = "type")
    FlightLegEntity toFlightLegEntity(PartnerReservationFlightsLeg partnerReservationFlightsLeg);
}
