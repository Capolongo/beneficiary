package br.com.livelo.orderflight.domain.dtos.pricing.request;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculatePrice{
	private String currency;
	private BigDecimal amount;
	private PricingCalculateFlight flight;
	private PricingCalculateTaxes taxes;
	private ArrayList<PricingCalculatePricesDescription> pricesDescription;
}
