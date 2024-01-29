package br.com.livelo.orderflight.domain.dtos.confirmation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmOrderPriceRequest {
    private BigDecimal pointsAmount;
}
