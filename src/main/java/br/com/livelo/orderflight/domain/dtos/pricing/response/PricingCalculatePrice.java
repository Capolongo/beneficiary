package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class PricingCalculatePrice {
    private String priceListId;
    private BigDecimal amount;
    private Integer pointsAmount;
    private PricingCalculateFlight flight;
    private PricingCalculateTaxes taxes;
    private Integer accrualPoints;
    private PricingCalculatePricesDescription pricesDescription;
}
