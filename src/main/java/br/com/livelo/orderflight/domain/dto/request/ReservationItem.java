package br.com.livelo.orderflight.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationItem {
    private String skuId;
    private String productId;
    private String commerceItemId;
    private Integer quantity;
    private String productType;
}
