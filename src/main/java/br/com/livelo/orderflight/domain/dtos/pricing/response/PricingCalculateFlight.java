package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateFlight {
    private BigDecimal amount;
    private Integer poIntegersAmount;
}
