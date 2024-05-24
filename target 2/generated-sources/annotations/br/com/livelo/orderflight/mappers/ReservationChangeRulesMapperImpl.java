package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationChangeRule;
import br.com.livelo.orderflight.domain.entity.ChangeRuleEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationChangeRulesMapperImpl implements ReservationChangeRulesMapper {

    @Override
    public ChangeRuleEntity toChangeRuleEntity(PartnerReservationChangeRule partnerReservationChangeRule) {
        if ( partnerReservationChangeRule == null ) {
            return null;
        }

        ChangeRuleEntity.ChangeRuleEntityBuilder<?, ?> changeRuleEntity = ChangeRuleEntity.builder();

        changeRuleEntity.description( partnerReservationChangeRule.getDescription() );
        changeRuleEntity.type( partnerReservationChangeRule.getType() );

        return changeRuleEntity.build();
    }
}
