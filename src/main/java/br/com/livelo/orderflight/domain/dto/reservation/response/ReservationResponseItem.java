package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.util.Set;

public record ReservationResponseItem (String commerceItemId,
										String skuId,
										String productId,
										Integer quantity,
										String externalCoupon,
										ReservationResponseOrderItemPrice price,
										ReservationResponseTravelInfo travelInfo,
										Set<ReservationResponseSegment> segments){

}
