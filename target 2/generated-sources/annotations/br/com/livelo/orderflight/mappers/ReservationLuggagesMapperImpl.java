package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationLuggage;
import br.com.livelo.orderflight.domain.entity.LuggageEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationLuggagesMapperImpl implements ReservationLuggagesMapper {

    @Override
    public LuggageEntity toReservationLuggageEntity(PartnerReservationLuggage partnerReservationLuggage) {
        if ( partnerReservationLuggage == null ) {
            return null;
        }

        LuggageEntity.LuggageEntityBuilder luggageEntity = LuggageEntity.builder();

        luggageEntity.description( partnerReservationLuggage.getDescription() );
        luggageEntity.type( partnerReservationLuggage.getType() );
        if ( partnerReservationLuggage.getQuantity() != null ) {
            luggageEntity.quantity( partnerReservationLuggage.getQuantity().longValue() );
        }
        luggageEntity.measurement( partnerReservationLuggage.getMeasurement() );
        if ( partnerReservationLuggage.getWeight() != null ) {
            luggageEntity.weight( partnerReservationLuggage.getWeight().doubleValue() );
        }

        return luggageEntity.build();
    }
}
