package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@ToString
public class PricingCalculatePricesDescription {
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private PricingCalculateFlight flight;
    private String passengerType;
    private List<PricingCalculateTaxes> taxes;
}
