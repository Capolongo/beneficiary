package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmOrderPriceResponse {
    public BigDecimal amount;
    public BigDecimal pointsAmount;
}
