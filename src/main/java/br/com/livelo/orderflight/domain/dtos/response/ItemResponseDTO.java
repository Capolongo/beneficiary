package br.com.livelo.orderflight.domain.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDTO {
    public String commerceItemId;
    public String skuId;
    public String productId;
    public int quantity;
    public PriceResponseDTO price;
}
