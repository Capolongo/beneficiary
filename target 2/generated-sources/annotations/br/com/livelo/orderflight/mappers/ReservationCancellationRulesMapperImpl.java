package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationCancellationRule;
import br.com.livelo.orderflight.domain.entity.CancellationRuleEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationCancellationRulesMapperImpl implements ReservationCancellationRulesMapper {

    @Override
    public CancellationRuleEntity toCancellationRuleEntity(PartnerReservationCancellationRule partnerReservationCancellationRule) {
        if ( partnerReservationCancellationRule == null ) {
            return null;
        }

        CancellationRuleEntity.CancellationRuleEntityBuilder cancellationRuleEntity = CancellationRuleEntity.builder();

        cancellationRuleEntity.description( partnerReservationCancellationRule.getDescription() );
        cancellationRuleEntity.type( partnerReservationCancellationRule.getType() );

        return cancellationRuleEntity.build();
    }
}
