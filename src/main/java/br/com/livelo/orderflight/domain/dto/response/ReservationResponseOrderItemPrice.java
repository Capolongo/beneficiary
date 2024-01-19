package br.com.livelo.orderflight.domain.dto.response;

import java.math.BigDecimal;

public record ReservationResponseOrderItemPrice (String listPrice,
												BigDecimal amount,
												BigDecimal pointsAmount,
												BigDecimal accrualPoints,
												BigDecimal partnerAmount,
												String priceListId,
												String priceRule){

}
