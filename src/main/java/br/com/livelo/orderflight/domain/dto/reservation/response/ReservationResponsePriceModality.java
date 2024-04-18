package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;

public record ReservationResponsePriceModality(Long id,
                                               BigDecimal amount,
                                               BigDecimal pointsAmount,
                                               Float multiplier,
                                               Float multiplierAccrual,
                                               Float markup,
                                               Double accrualPoints,
                                               String priceListId) {
}
