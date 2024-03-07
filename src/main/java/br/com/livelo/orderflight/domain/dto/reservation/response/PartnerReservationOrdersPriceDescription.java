package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

import java.util.List;

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
