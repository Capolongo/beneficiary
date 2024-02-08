package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;

public record ReservationResponseOrderStatus(String code,
		String description,
		String partnerCode,
		String partnerDescription,
		String partnerResponse,
		LocalDateTime statusDate){

}
