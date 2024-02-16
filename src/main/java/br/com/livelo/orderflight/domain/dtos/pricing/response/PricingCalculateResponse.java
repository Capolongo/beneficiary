package br.com.livelo.orderflight.domain.dtos.pricing.response;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingCalculateResponse {
    private String id;
    private ArrayList<PricingCalculatePrice> prices;
    private String priceRule;
}
