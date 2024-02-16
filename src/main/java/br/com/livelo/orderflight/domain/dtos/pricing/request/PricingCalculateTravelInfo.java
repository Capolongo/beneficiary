package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateTravelInfo{
	private String type;
	private Integer adultQuantity;
	private Integer childQuantity;
	private Integer babyQuantity;
	private String typeClass;
	private String stageJourney;
}
