package br.com.livelo.orderflight.domain.dtos.pricing.request;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculatePricesDescription {
	private BigDecimal amount;
	private PricingCalculateFlight flight;
	private String passengerType;
	private Integer passengerCount;
	private ArrayList<PricingCalculateTaxes> taxes;
}
