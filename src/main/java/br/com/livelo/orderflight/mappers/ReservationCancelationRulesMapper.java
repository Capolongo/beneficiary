package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationCancelationRule;
import br.com.livelo.orderflight.domain.entity.CancelationRuleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationCancelationRulesMapper {
    CancelationRuleEntity toCancelationRuleEntity(PartnerReservationCancelationRule partnerReservationCancelationRule);
}
