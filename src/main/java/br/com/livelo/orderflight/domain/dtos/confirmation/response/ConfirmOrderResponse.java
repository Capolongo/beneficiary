package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmOrderResponse {
    public String id;
    public String commerceOrderId;
    public String partnerOrderId;
    public String partnerCode;
    public String submittedDate;
    public String expirationDate;
    public String transactionId;
    public ConfirmOrderStatusResponse status;
    public ConfirmOrderPriceResponse price;
    public Set<ConfirmOrderItemResponse> items;
}
