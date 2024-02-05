package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.time.LocalDateTime;
import java.util.Set;

public record ReservationResponse(String commerceOrderId,
                                  String partnerOrderId,
                                  String partnerCode,
                                  LocalDateTime submittedDate,
                                  String channel,
                                  String tierCode,
                                  String originOrder,
                                  String customerIdentifier,
                                  String transactionId,
                                  LocalDateTime expirationDate,
                                  int expirationTimer,
                                  ReservationResponsePrice price,
                                  Set<ReservationResponseItem> items,
                                  Set<ReservationResponseOrderStatus> statusHistory,
                                  ReservationResponseOrderStatus status) {
}
