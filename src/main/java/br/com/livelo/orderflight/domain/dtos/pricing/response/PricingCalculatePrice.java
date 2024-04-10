package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculatePrice {
    private String priceListId;
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private PricingCalculateFlight flight;
    private PricingCalculateTaxes taxes;
    private BigDecimal accrualPoints;
    private PricingCalculatePricesDescription pricesDescription;
}
