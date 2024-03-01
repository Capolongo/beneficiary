package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculateFlight {
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private String passengerType;
    private Integer passengerCount;
}
