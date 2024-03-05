package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryCommerceItemDeliveryDTO implements Serializable {
    private String id;
    private Integer quantity;
    private String commerceItemId;
}
