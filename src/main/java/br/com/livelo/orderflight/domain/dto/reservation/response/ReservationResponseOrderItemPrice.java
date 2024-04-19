package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.Set;

public record ReservationResponseOrderItemPrice(String listPrice,
                                                BigDecimal amount,
                                                BigDecimal pointsAmount,
                                                BigDecimal accrualPoints,
                                                BigDecimal partnerAmount,
                                                Float multiplier,
                                                Float multiplierAccrual,
                                                Float markup,
                                                String priceListId,
                                                String priceRule,
                                                Set<ReservationResponsePriceModality> pricesModalities) {

}
