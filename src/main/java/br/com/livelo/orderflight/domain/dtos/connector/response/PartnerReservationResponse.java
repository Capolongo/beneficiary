package br.com.livelo.orderflight.domain.dtos.connector.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerReservationResponse {
    private String commerceOrderId;
    private String partnerOrderId;
    private PartnerReservationStatus status;

}
