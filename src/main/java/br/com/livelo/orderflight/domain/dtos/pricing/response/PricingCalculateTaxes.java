package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class PricingCalculateTaxes{
    private String type;
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private Float multiplier;
    private Float multiplierAccrual;
    private Float markup;
}
