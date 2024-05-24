package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerResponseStatus;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusHistoryEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationStatusMapperImpl implements ReservationStatusMapper {

    @Override
    public OrderCurrentStatusEntity toOrderStatus(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }

        OrderCurrentStatusEntity.OrderCurrentStatusEntityBuilder orderCurrentStatusEntity = OrderCurrentStatusEntity.builder();

        orderCurrentStatusEntity.code( partnerReservationResponseCurrentStatusCode( partnerReservationResponse ) );
        orderCurrentStatusEntity.description( partnerReservationResponseCurrentStatusDescription( partnerReservationResponse ) );
        orderCurrentStatusEntity.partnerCode( partnerReservationResponseCurrentStatusPartnerCode( partnerReservationResponse ) );
        orderCurrentStatusEntity.partnerDescription( partnerReservationResponseCurrentStatusPartnerDescription( partnerReservationResponse ) );

        return orderCurrentStatusEntity.build();
    }

    @Override
    public OrderStatusHistoryEntity toOrderStatusHistory(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }

        OrderStatusHistoryEntity.OrderStatusHistoryEntityBuilder orderStatusHistoryEntity = OrderStatusHistoryEntity.builder();

        orderStatusHistoryEntity.code( partnerReservationResponseCurrentStatusCode( partnerReservationResponse ) );
        orderStatusHistoryEntity.description( partnerReservationResponseCurrentStatusDescription( partnerReservationResponse ) );
        orderStatusHistoryEntity.partnerCode( partnerReservationResponseCurrentStatusPartnerCode( partnerReservationResponse ) );
        orderStatusHistoryEntity.partnerDescription( partnerReservationResponseCurrentStatusPartnerDescription( partnerReservationResponse ) );

        return orderStatusHistoryEntity.build();
    }

    private String partnerReservationResponseCurrentStatusCode(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }
        PartnerResponseStatus currentStatus = partnerReservationResponse.getCurrentStatus();
        if ( currentStatus == null ) {
            return null;
        }
        String code = currentStatus.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String partnerReservationResponseCurrentStatusDescription(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }
        PartnerResponseStatus currentStatus = partnerReservationResponse.getCurrentStatus();
        if ( currentStatus == null ) {
            return null;
        }
        String description = currentStatus.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }

    private String partnerReservationResponseCurrentStatusPartnerCode(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }
        PartnerResponseStatus currentStatus = partnerReservationResponse.getCurrentStatus();
        if ( currentStatus == null ) {
            return null;
        }
        String partnerCode = currentStatus.getPartnerCode();
        if ( partnerCode == null ) {
            return null;
        }
        return partnerCode;
    }

    private String partnerReservationResponseCurrentStatusPartnerDescription(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }
        PartnerResponseStatus currentStatus = partnerReservationResponse.getCurrentStatus();
        if ( currentStatus == null ) {
            return null;
        }
        String partnerDescription = currentStatus.getPartnerDescription();
        if ( partnerDescription == null ) {
            return null;
        }
        return partnerDescription;
    }
}
