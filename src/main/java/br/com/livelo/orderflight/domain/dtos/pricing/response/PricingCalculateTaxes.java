package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class PricingCalculateTaxes {
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private String description;
}
