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
    private Integer pointsAmount;
    private PricingCalculateFlight flight;
    private PricingCalculateTaxes taxes;
    private Integer accrualPoints;
    private PricingCalculatePricesDescription pricesDescription;
    private Float multiplier;
    private Double multiplierAccrual;
    private Float markup;
}
