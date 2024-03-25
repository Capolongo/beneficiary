package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;

public record ReservationResponseOrderItemPrice (String listPrice,
												 BigDecimal amount,
												 BigDecimal pointsAmount,
												 BigDecimal accrualPoints,
												 BigDecimal partnerAmount,
												 String priceListId,
												 String priceRule,
												 List<ReservationResponsePriceModality> pricesModalities){

}
