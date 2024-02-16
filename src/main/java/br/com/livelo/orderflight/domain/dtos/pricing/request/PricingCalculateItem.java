package br.com.livelo.orderflight.domain.dtos.pricing.request;

import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateItem {
	private String id;
	private String flightType;
	private PricingCalculatePrice price;
	private ArrayList<PricingCalculateSegment> segments;
}
