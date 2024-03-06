package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class PricingCalculateResponse {
    private String id;
    private List<PricingCalculatePrice> prices;
    private String priceRule;
}
