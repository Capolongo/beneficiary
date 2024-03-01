package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.entity.AirlineEntity;
import br.com.livelo.orderflight.domain.entity.AirlineManagedByEntity;
import br.com.livelo.orderflight.domain.entity.AirlineOperatedByEntity;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReservationFlightLegMapper {
    @Mapping(target = "flightNumber", source = "flightNumber")
    @Mapping(target = "flightDuration", source = "flightDuration")
    @Mapping(target = "airline", source = "partnerReservationFlightsLeg", qualifiedByName = "buildAirline")
    @Mapping(target = "timeToWait", source = "timeToWait")
    @Mapping(target = "originIata", source = "originIata")
    @Mapping(target = "originDescription", source = "originDescription")
    @Mapping(target = "destinationIata", source = "destinationIata")
    @Mapping(target = "destinationDescription", source = "destinationDescription")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "departureDate", source = "departureDate",dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Mapping(target = "arrivalDate", source = "arrivalDate",dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    FlightLegEntity toFlightLegEntity(PartnerReservationFlightsLeg partnerReservationFlightsLeg);

    @Named("buildAirline")
    default AirlineEntity buildAirline(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        return AirlineEntity.builder()
                .managedBy(AirlineManagedByEntity.builder()
                        .iata(partnerReservationFlightsLeg.getAirline().getManagedBy().getIata())
                        .description(partnerReservationFlightsLeg.getAirline().getManagedBy().getDescription())
                        .build())
                .operatedBy(AirlineOperatedByEntity.builder()
                        .iata(partnerReservationFlightsLeg.getAirline().getOperatedBy().getIata())
                        .description(partnerReservationFlightsLeg.getAirline().getOperatedBy().getDescription())
                        .build())
                .build();
    }
}
