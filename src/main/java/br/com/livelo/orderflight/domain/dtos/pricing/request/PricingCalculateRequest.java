package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PricingCalculateRequest {
    private PricingCalculateTravelInfo travelInfo;
    private List<PricingCalculateItem> items;
}
