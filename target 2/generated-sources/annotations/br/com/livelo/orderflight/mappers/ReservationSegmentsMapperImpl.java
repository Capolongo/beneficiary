package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationSegmentsMapperImpl implements ReservationSegmentsMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss" );

    @Override
    public SegmentEntity toSegmentEntity(PartnerReservationSegment partnerReservationSegment) {
        if ( partnerReservationSegment == null ) {
            return null;
        }

        SegmentEntity.SegmentEntityBuilder segmentEntity = SegmentEntity.builder();

        segmentEntity.airlineIata( partnerReservationSegmentAirlineIata( partnerReservationSegment ) );
        segmentEntity.airlineDescription( partnerReservationSegmentAirlineDescription( partnerReservationSegment ) );
        if ( partnerReservationSegment.getDepartureDate() != null ) {
            segmentEntity.departureDate( LocalDateTime.parse( partnerReservationSegment.getDepartureDate(), dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 ) );
        }
        if ( partnerReservationSegment.getArrivalDate() != null ) {
            segmentEntity.arrivalDate( LocalDateTime.parse( partnerReservationSegment.getArrivalDate(), dateTimeFormatter_yyyy_MM_dd_T_HH_mm_ss_11798231098 ) );
        }
        segmentEntity.partnerId( partnerReservationSegment.getPartnerId() );
        segmentEntity.step( partnerReservationSegment.getStep() );
        segmentEntity.stops( partnerReservationSegment.getStops() );
        segmentEntity.flightDuration( partnerReservationSegment.getFlightDuration() );
        segmentEntity.originIata( partnerReservationSegment.getOriginIata() );
        segmentEntity.destinationIata( partnerReservationSegment.getDestinationIata() );
        segmentEntity.originCity( partnerReservationSegment.getOriginCity() );
        segmentEntity.originAirport( partnerReservationSegment.getOriginAirport() );
        segmentEntity.destinationAirport( partnerReservationSegment.getDestinationAirport() );
        segmentEntity.destinationCity( partnerReservationSegment.getDestinationCity() );
        segmentEntity.cabinClass( partnerReservationSegment.getCabinClass() );

        segmentEntity.luggages( mapLuggages(partnerReservationSegment) );
        segmentEntity.cancellationRules( mapCancellationRules(partnerReservationSegment) );
        segmentEntity.changeRules( mapChangeRules(partnerReservationSegment) );
        segmentEntity.flightsLegs( mapFlightLeg(partnerReservationSegment) );

        return segmentEntity.build();
    }

    private String partnerReservationSegmentAirlineIata(PartnerReservationSegment partnerReservationSegment) {
        if ( partnerReservationSegment == null ) {
            return null;
        }
        PartnerReservationAirline airline = partnerReservationSegment.getAirline();
        if ( airline == null ) {
            return null;
        }
        String iata = airline.getIata();
        if ( iata == null ) {
            return null;
        }
        return iata;
    }

    private String partnerReservationSegmentAirlineDescription(PartnerReservationSegment partnerReservationSegment) {
        if ( partnerReservationSegment == null ) {
            return null;
        }
        PartnerReservationAirline airline = partnerReservationSegment.getAirline();
        if ( airline == null ) {
            return null;
        }
        String description = airline.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }
}
