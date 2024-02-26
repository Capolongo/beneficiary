package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PricingCalculatePrice {
    private String currency;
    private BigDecimal amount;
    private PricingCalculateFlight flight;
    private PricingCalculateTaxes taxes;
    private PricingCalculatePricesDescription pricesDescription;
}
