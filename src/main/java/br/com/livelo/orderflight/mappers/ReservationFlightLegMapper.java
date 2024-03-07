package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationFlightLegMapper {
    @Mapping(target = "flightNumber", source = "flightNumber")
    @Mapping(target = "flightDuration", source = "flightDuration")
    @Mapping(target = "airlineManagedByIata", source = "partnerReservationFlightsLeg.airline.managedBy.iata")
    @Mapping(target = "airlineManagedByDescription", source = "partnerReservationFlightsLeg.airline.managedBy.description")
    @Mapping(target = "airlineOperatedByIata", source = "partnerReservationFlightsLeg.airline.operatedBy.iata")
    @Mapping(target = "airlineOperatedByDescription", source = "partnerReservationFlightsLeg.airline.operatedBy.description")
    @Mapping(target = "timeToWait", source = "timeToWait")
    @Mapping(target = "originIata", source = "originIata")
    @Mapping(target = "originDescription", source = "originDescription")
    @Mapping(target = "destinationIata", source = "destinationIata")
    @Mapping(target = "destinationDescription", source = "destinationDescription")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "departureDate", source = "departureDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Mapping(target = "arrivalDate", source = "arrivalDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    FlightLegEntity toFlightLegEntity(PartnerReservationFlightsLeg partnerReservationFlightsLeg);
}
