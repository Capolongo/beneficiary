package br.com.livelo.orderflight.domain.dtos.connector.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorReservationStatus {
    private String code;
    private String description;
    private String partnerCode;
    private String partnerDescription;
}