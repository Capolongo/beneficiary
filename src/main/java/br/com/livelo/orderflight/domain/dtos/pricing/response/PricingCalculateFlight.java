package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricingCalculateFlight {
    private BigDecimal pointsAmount;
    private BigDecimal amount;
    private Float multiplier;
    private Float multiplierAccrual;
    private Float markup;
    private String passengerType;
    private Integer passengerCount;
}
