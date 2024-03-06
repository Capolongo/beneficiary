package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateCancellationRule {
	private String description;
}