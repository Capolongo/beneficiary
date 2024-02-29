package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class PricingCalculatePricesDescription {
    private List<PricingCalculateFlight> flights;
    private List<PricingCalculateTaxes> taxes;
}
