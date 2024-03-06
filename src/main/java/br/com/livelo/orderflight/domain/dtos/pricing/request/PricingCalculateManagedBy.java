package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateManagedBy{
	private String iata;
	private String description;
}
