package br.com.livelo.orderflight.domain.dtos.connector.response;

import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorConfirmOrderResponse {
    private String partnerOrderId;
    private String partnerCode;
    private String submittedDate;
    private String expirationDate;
    private String transactionId;
    private OrderStatusDTO currentStatus;
    private String voucher;
}
