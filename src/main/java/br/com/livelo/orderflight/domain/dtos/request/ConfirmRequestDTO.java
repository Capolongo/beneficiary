package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmRequestDTO {
    private String id;
    private String commerceOrderId;
    private String commerceItemId;
    private String partnerCode;
    private String customerId;
    private String partnerOrderId;
    private String submittedDate;
    private String channel;
    private String originOfOrder;
    private AmountDTO amount;
    private Set<ItemRequestDTO> items;
}
