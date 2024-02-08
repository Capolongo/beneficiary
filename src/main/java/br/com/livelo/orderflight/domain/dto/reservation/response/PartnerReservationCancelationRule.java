package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationCancelationRule {
	private String type;
	private String description;
}
