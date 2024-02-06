package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmOrderResponse {
    private String id;
    private String commerceOrderId;
    private String partnerOrderId;
    private String partnerCode;
    private String submittedDate;
    private String expirationDate;
    private String transactionId;
    private ConfirmOrderStatusResponse status;
    private ConfirmOrderPriceResponse price;
    private Set<ConfirmOrderItemResponse> items;
}
