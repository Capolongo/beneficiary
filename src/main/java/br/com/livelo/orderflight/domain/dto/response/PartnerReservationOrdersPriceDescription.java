package br.com.livelo.orderflight.domain.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationOrdersPriceDescription {
	private BigDecimal amount;
    private String type;
    private String description;
}
