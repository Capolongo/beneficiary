package br.com.livelo.orderflight.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationLuggage {
	private String type;
	private String description;
}
