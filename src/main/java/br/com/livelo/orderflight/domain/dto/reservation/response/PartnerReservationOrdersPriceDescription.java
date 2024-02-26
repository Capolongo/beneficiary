package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationOrdersPriceDescription {
    private List<PartnerReservationOrdersPriceDescriptionFlight> flights;
    private List<PartnerReservationOrdersPriceDescriptionTaxes> taxes;
}
