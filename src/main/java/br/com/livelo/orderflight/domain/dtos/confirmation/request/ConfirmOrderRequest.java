package br.com.livelo.orderflight.domain.dtos.confirmation.request;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmOrderRequest {
    private String id;
    private String commerceOrderId;
    private String commerceItemId;
    private String partnerCode;
    private String customerId;
    private String partnerOrderId;
    private String submittedDate;
    private String channel;
    private String originOfOrder;
    private ConfirmOrderPriceRequest price;
    private Set<ConfirmOrderItemRequest> items;
}
