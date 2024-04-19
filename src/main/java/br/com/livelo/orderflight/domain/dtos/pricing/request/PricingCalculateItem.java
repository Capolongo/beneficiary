package br.com.livelo.orderflight.domain.dtos.pricing.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PricingCalculateItem {
    private String id;
    private String flightType;
    private String partnerCode;
    private PricingCalculatePrice price;
    private List<PricingCalculateSegment> segments;
}
