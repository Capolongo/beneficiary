package br.com.livelo.orderflight.domain.dto;

import java.time.LocalDateTime;

//TODO FINALIZAR DTO BASEADO NO ORDERENTITY
public record ReservationResponse(String commerceOrderId,
                                  String partnerOrderId,
                                  String partnerCode,
                                  LocalDateTime submittedDate,
                                  String channel,
                                  String tierCode,
                                  String originOrder,
                                  String customerIdentifier,
                                  String transactionId,
                                  LocalDateTime expirationDate) {
}
