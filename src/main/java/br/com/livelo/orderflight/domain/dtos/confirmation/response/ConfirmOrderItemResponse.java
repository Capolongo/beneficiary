package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmOrderItemResponse {
    public String commerceItemId;
    public String skuId;
    public String productId;
    public int quantity;
    public ConfirmOrderPriceResponse price;
}
