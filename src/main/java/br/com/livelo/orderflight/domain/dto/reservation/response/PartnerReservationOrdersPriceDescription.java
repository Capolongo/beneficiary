package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PartnerReservationOrdersPriceDescription {
	private BigDecimal amount;
    private String type;
    private String description;
    private List<PartnerReservationOrdersPriceDescriptionTaxes> taxes;
}
