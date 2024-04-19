package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationOrdersPriceDescriptionFlight {
    private BigDecimal amount;
    private String passengerType;
    private Integer passengerCount;
}
