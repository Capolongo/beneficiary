package br.com.livelo.orderflight.domain.dto.reservation.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PartnerReservationItem {
    private String productType;
}
