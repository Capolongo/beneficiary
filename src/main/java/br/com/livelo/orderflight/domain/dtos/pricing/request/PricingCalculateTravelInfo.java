package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateTravelInfo{
	private String type;
	private Integer adt;
	private Integer chd;
	private Integer inf;
	private String cabinClass;
	private String stageJourney;
}
