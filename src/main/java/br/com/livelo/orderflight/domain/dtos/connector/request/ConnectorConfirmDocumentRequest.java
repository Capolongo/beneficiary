package br.com.livelo.orderflight.domain.dtos.connector.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorConfirmDocumentRequest {
    private String number;
    private String type;
}
