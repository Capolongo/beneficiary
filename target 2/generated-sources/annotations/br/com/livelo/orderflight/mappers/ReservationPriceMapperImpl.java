package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationPriceMapperImpl implements ReservationPriceMapper {

    @Override
    public OrderPriceEntity toOrderPriceEntity(PartnerReservationResponse partnerReservationResponse, String listPrice) {
        if ( partnerReservationResponse == null && listPrice == null ) {
            return null;
        }

        OrderPriceEntity.OrderPriceEntityBuilder orderPriceEntity = OrderPriceEntity.builder();

        if ( partnerReservationResponse != null ) {
            orderPriceEntity.partnerAmount( partnerReservationResponse.getAmount() );
            orderPriceEntity.amount( partnerReservationResponse.getAmount() );
        }
        orderPriceEntity.priceListId( listPrice );
        orderPriceEntity.ordersPriceDescription( mapOrdersPriceDescription(partnerReservationResponse) );

        return orderPriceEntity.build();
    }
}
