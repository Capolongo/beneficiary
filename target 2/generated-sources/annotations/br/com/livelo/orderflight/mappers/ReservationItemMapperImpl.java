package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationItemMapperImpl implements ReservationItemMapper {

    @Override
    public OrderItemEntity toOrderItemEntity(ReservationRequest reservationRequest, ReservationItem reservationItem, PartnerReservationItem partnerReservationItem, String listPrice) {
        if ( reservationRequest == null && reservationItem == null && partnerReservationItem == null && listPrice == null ) {
            return null;
        }

        OrderItemEntity.OrderItemEntityBuilder orderItemEntity = OrderItemEntity.builder();

        if ( reservationItem != null ) {
            orderItemEntity.commerceItemId( reservationItem.getCommerceItemId() );
            orderItemEntity.productId( reservationItem.getProductId() );
            orderItemEntity.skuId( reservationItem.getSkuId() );
            orderItemEntity.quantity( reservationItem.getQuantity() );
            orderItemEntity.productType( reservationItem.getProductType() );
        }
        orderItemEntity.price( mapPrice(partnerReservationItem, listPrice) );
        orderItemEntity.travelInfo( mapTravelInfo(reservationRequest, partnerReservationItem) );
        orderItemEntity.segments( mapSegments(partnerReservationItem) );

        return orderItemEntity.build();
    }
}
