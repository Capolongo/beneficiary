package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemRequestDTO {
    public String commerceItemId;
    public String skuId;
    public String productId;
    public String externalCoupon;
    public int quantity;
    public int pointsAmount;
}
