package br.com.livelo.orderflight.domain.dtos;

import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorPartnerConfirmationDTO {
    private String partnerOrderId;
    private String partnerCode;
    private String submittedDate;
    private String expirationDate;
    private String transactionId;
    private OrderStatusEntity currentStatus;
    private String voucher;
}
