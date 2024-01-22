package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;

public record ReservationResponsePriceDescription (BigDecimal amount,
												   BigDecimal pointsAmount,
												   String type,
												   String description){

}
