package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationOrderPriceDescriptionMapperImpl implements ReservationOrderPriceDescriptionMapper {

    @Override
    public OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight) {
        if ( partnerReservationOrdersPriceDescriptionFlight == null ) {
            return null;
        }

        OrderPriceDescriptionEntity.OrderPriceDescriptionEntityBuilder orderPriceDescriptionEntity = OrderPriceDescriptionEntity.builder();

        orderPriceDescriptionEntity.type( partnerReservationOrdersPriceDescriptionFlight.getPassengerType() );
        orderPriceDescriptionEntity.amount( partnerReservationOrdersPriceDescriptionFlight.getAmount() );

        return orderPriceDescriptionEntity.build();
    }
}
