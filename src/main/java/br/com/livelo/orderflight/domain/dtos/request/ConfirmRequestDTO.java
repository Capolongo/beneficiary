package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmRequestDTO {
    public String id;
    public String commerceOrderId;
    public String commerceItemId;
    public String partnerCode;
    public String customerId;
    public String partnerOrderId;
    public String submittedDate;
    public String channel;
    public String originOfOrder;
    public AmountDTO amount;
    public Set<ItemRequestDTO> items;
}
