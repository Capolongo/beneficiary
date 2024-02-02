package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmOrderItemResponse {
    private String commerceItemId;
    private String skuId;
    private String productId;
    private int quantity;
    private String externalCoupon;
    private String productType;
    private ConfirmOrderPriceResponse price;
    private ConfirmationOrderTravelInfoResponse travelInfo;
    private Set<ConfirmationOrderSegmentsResponse> segments;
}
