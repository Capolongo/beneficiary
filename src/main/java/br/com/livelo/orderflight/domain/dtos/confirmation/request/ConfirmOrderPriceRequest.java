package br.com.livelo.orderflight.domain.dtos.confirmation.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ConfirmOrderPriceRequest {
    private BigDecimal pointsAmount;
}
