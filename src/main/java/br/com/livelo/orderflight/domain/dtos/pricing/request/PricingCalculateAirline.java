package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateAirline{
	private String iata;
	private String description;
	private PricingCalculateManagedBy managedBy;
	private PricingCalculateOperatedBy operatedBy;
}
