package br.com.livelo.orderflight.domain.dtos;

import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@Builder
public class ConnectorPartnerConfirmationDTO {
    private String partnerOrderId;
    private String partnerCode;
    private ZonedDateTime submittedDate;
    private ZonedDateTime expirationDate;
    private String transactionId;
    private OrderStatusEntity currentStatus;
    private String voucher;
}
