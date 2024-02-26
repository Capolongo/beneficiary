package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationOrdersPriceDescriptionTaxes {
	private BigDecimal amount;
    private String type;
    private String description;
}
