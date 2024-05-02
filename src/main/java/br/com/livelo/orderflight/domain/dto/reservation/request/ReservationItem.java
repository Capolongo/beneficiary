package br.com.livelo.orderflight.domain.dto.reservation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ReservationItem {
    private String skuId;
    private String productId;
    private String commerceItemId;
    private Integer quantity;
    private String productType;
}
