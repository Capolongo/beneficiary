package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationChangeRule;
import br.com.livelo.orderflight.domain.entity.ChangeRuleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationChangeRulesMapper {
    ChangeRuleEntity toChangeRuleEntity(PartnerReservationChangeRule partnerReservationChangeRule);
}
