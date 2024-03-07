package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricingCalculateFlight {
    private BigDecimal pointsAmount;
    private BigDecimal amount;
    private String passengerType;
    private Integer passengerCount;
}
