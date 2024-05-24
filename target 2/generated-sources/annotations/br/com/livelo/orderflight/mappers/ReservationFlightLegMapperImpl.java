package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightLegAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationFlightLegMapperImpl implements ReservationFlightLegMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss" );

    @Override
    public FlightLegEntity toFlightLegEntity(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        if ( partnerReservationFlightsLeg == null ) {
            return null;
        }

        FlightLegEntity.FlightLegEntityBuilder flightLegEntity = FlightLegEntity.builder();

        flightLegEntity.flightNumber( partnerReservationFlightsLeg.getFlightNumber() );
        flightLegEntity.flightDuration( partnerReservationFlightsLeg.getFlightDuration() );
        flightLegEntity.airlineManagedByIata( partnerReservationFlightsLegAirlineManagedByIata( partnerReservationFlightsLeg ) );
        flightLegEntity.airlineManagedByDescription( partnerReservationFlightsLegAirlineManagedByDescription( partnerReservationFlightsLeg ) );
        flightLegEntity.airlineOperatedByIata( partnerReservationFlightsLegAirlineOperatedByIata( partnerReservationFlightsLeg ) );
        flightLegEntity.airlineOperatedByDescription( partnerReservationFlightsLegAirlineOperatedByDescription( partnerReservationFlightsLeg ) );
        if ( partnerReservationFlightsLeg.getTimeToWait() != null ) {
            flightLegEntity.timeToWait( Integer.parseInt( partnerReservationFlightsLeg.getTimeToWait() ) );
        }
        flightLegEntity.originIata( partnerReservationFlightsLeg.getOriginIata() );
        flightLegEntity.destinationIata( partnerReservationFlightsLeg.getDestinationIata() );
        flightLegEntity.type( partnerReservationFlightsLeg.getType() );
        if ( partnerReservationFlightsLeg.getDepartureDate() != null ) {
            flightLegEntity.departureDate( LocalDateTime.parse( partnerReservationFlightsLeg.getDepartureDate(), dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 ) );
        }
        if ( partnerReservationFlightsLeg.getArrivalDate() != null ) {
            flightLegEntity.arrivalDate( LocalDateTime.parse( partnerReservationFlightsLeg.getArrivalDate(), dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 ) );
        }
        flightLegEntity.originCity( partnerReservationFlightsLeg.getOriginCity() );
        flightLegEntity.originAirport( partnerReservationFlightsLeg.getOriginAirport() );
        flightLegEntity.destinationAirport( partnerReservationFlightsLeg.getDestinationAirport() );
        flightLegEntity.destinationCity( partnerReservationFlightsLeg.getDestinationCity() );
        flightLegEntity.fareClass( partnerReservationFlightsLeg.getFareClass() );
        flightLegEntity.aircraftCode( partnerReservationFlightsLeg.getAircraftCode() );

        return flightLegEntity.build();
    }

    private String partnerReservationFlightsLegAirlineManagedByIata(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        if ( partnerReservationFlightsLeg == null ) {
            return null;
        }
        PartnerReservationFlightLegAirline airline = partnerReservationFlightsLeg.getAirline();
        if ( airline == null ) {
            return null;
        }
        PartnerReservationAirline managedBy = airline.getManagedBy();
        if ( managedBy == null ) {
            return null;
        }
        String iata = managedBy.getIata();
        if ( iata == null ) {
            return null;
        }
        return iata;
    }

    private String partnerReservationFlightsLegAirlineManagedByDescription(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        if ( partnerReservationFlightsLeg == null ) {
            return null;
        }
        PartnerReservationFlightLegAirline airline = partnerReservationFlightsLeg.getAirline();
        if ( airline == null ) {
            return null;
        }
        PartnerReservationAirline managedBy = airline.getManagedBy();
        if ( managedBy == null ) {
            return null;
        }
        String description = managedBy.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private String partnerReservationFlightsLegAirlineOperatedByIata(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        if ( partnerReservationFlightsLeg == null ) {
            return null;
        }
        PartnerReservationFlightLegAirline airline = partnerReservationFlightsLeg.getAirline();
        if ( airline == null ) {
            return null;
        }
        PartnerReservationAirline operatedBy = airline.getOperatedBy();
        if ( operatedBy == null ) {
            return null;
        }
        String iata = operatedBy.getIata();
        if ( iata == null ) {
            return null;
        }
        return iata;
    }

    private String partnerReservationFlightsLegAirlineOperatedByDescription(PartnerReservationFlightsLeg partnerReservationFlightsLeg) {
        if ( partnerReservationFlightsLeg == null ) {
            return null;
        }
        PartnerReservationFlightLegAirline airline = partnerReservationFlightsLeg.getAirline();
        if ( airline == null ) {
            return null;
        }
        PartnerReservationAirline operatedBy = airline.getOperatedBy();
        if ( operatedBy == null ) {
            return null;
        }
        String description = operatedBy.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }
}
