package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateTaxes {
    private BigDecimal amount;
    private Integer pointsAmount;
    private String description;
}
