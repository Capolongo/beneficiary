package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateLuggage{
	private String type;
	private Integer quantity;
	private Integer weight;
	private String measurement;
	private String description;
}
