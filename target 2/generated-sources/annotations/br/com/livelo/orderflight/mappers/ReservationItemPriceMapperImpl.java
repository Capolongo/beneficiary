package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationItemPriceMapperImpl implements ReservationItemPriceMapper {

    @Override
    public OrderItemPriceEntity toOrderItemPriceEntity(PartnerReservationItem partnerReservationItem, String priceList) {
        if ( partnerReservationItem == null && priceList == null ) {
            return null;
        }

        OrderItemPriceEntity.OrderItemPriceEntityBuilder orderItemPriceEntity = OrderItemPriceEntity.builder();

        if ( partnerReservationItem != null ) {
            orderItemPriceEntity.partnerAmount( partnerReservationItem.getAmount() );
            orderItemPriceEntity.amount( partnerReservationItem.getAmount() );
        }
        orderItemPriceEntity.priceListId( priceList );
        orderItemPriceEntity.accrualPoints( java.math.BigDecimal.ZERO );

        return orderItemPriceEntity.build();
    }
}
