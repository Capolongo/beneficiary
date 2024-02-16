package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculatePrice {
    private String priceListId;
    private BigDecimal amount;
    private Integer pointsAmount;
    private PricingCalculateFlight flight;
    private PricingCalculateTaxes taxes;
    private Integer accrualPoints;
    private ArrayList<PricingCalculatePricesDescription> pricesDescription;
}
