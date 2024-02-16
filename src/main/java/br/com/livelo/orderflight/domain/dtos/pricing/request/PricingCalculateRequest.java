package br.com.livelo.orderflight.domain.dtos.pricing.request;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateRequest {
	private PricingCalculateTravelInfo travelInfo;
	private ArrayList<PricingCalculateItem> items;
}
