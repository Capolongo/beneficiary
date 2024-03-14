package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculatePricesDescription {
    private List<PricingCalculateFlight> flights;
    private List<PricingCalculateTaxes> taxes;
}
