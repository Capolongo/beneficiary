package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationCancellationRule;
import br.com.livelo.orderflight.domain.entity.CancellationRuleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationCancellationRulesMapper {
    CancellationRuleEntity toCancellationRuleEntity(PartnerReservationCancellationRule partnerReservationCancellationRule);
}
