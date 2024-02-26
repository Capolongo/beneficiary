package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationFlightLegAirline {
    private PartnerReservationAirline managedBy;
    private PartnerReservationAirline operatedBy;
}
