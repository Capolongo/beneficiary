package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.math.BigDecimal;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculatePricesDescription {
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private PricingCalculateFlight flight;
    private String passengerType;
    private ArrayList<PricingCalculateTaxes> taxes;
}
