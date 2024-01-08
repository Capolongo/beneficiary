package br.com.livelo.orderflight.domain.dto;

import java.time.LocalDateTime;

public record CartResponse(String commerceOrderId,
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
