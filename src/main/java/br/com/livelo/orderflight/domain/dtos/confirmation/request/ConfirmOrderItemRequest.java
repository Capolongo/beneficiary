package br.com.livelo.orderflight.domain.dtos.confirmation.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConfirmOrderItemRequest {
    private String commerceItemId;
    private String skuId;
    private String productId;
    private String externalCoupon;
    private int quantity;
    private BigDecimal pointsAmount;
}
