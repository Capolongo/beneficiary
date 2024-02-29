package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateFlight{
    private BigDecimal pointsAmount;
    private BigDecimal amount;
    private String passengerType;
    private Integer passengerCount;
}
