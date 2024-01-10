package br.com.livelo.orderflight.domain.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmResponseDTO {
    public String id;
    public String commerceOrderId;
    public String partnerOrderId;
    public String partnerCode;
    public String submittedDate;
    public String expirationDate;
    public String transactionId;
    public StatusResponseDTO status;
    public PriceResponseDTO price;
    public Set<ItemResponseDTO> items;
}
