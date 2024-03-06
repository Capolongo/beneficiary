package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PricingCalculatePricesDescription {
    private List<PricingCalculateFlight> flights;
    private List<PricingCalculateTaxes> taxes;
}
