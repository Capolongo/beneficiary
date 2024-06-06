package br.com.livelo.orderflight.domain.dtos.connector.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerConfirmOrderResponse {
    private String partnerOrderId;
    private String partnerCode;
    private String submittedDate;
    private String expirationDate;
    private String transactionId;
    private PartnerConfirmOrderStatusResponse currentStatus;
    private String voucher;
}
