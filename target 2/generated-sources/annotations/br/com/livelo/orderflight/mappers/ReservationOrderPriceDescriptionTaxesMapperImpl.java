package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationOrderPriceDescriptionTaxesMapperImpl implements ReservationOrderPriceDescriptionTaxesMapper {

    @Override
    public OrderPriceDescriptionEntity toOrderPriceDescriptionEntity(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes) {
        if ( partnerReservationOrdersPriceDescriptionTaxes == null ) {
            return null;
        }

        OrderPriceDescriptionEntity.OrderPriceDescriptionEntityBuilder orderPriceDescriptionEntity = OrderPriceDescriptionEntity.builder();

        orderPriceDescriptionEntity.amount( partnerReservationOrdersPriceDescriptionTaxes.getAmount() );
        orderPriceDescriptionEntity.type( partnerReservationOrdersPriceDescriptionTaxes.getType() );

        return orderPriceDescriptionEntity.build();
    }
}
