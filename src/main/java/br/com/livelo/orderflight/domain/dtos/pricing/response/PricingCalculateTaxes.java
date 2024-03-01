package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculateTaxes {
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private String type;
}
