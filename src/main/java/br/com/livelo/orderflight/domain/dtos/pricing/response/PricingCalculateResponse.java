package br.com.livelo.orderflight.domain.dtos.pricing.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PricingCalculateResponse {
    private String id;
    private List<PricingCalculatePrice> prices;
    private String priceRuleName;
    private String priceRulePayload;
}
