package br.com.livelo.orderflight.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationChangeRule {
	private String type;
	private String description;
}
