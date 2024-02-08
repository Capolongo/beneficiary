package br.com.livelo.orderflight.domain.dtos.connector.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorConfirmOrderRequest {
  private String id;
  private String commerceOrderId;
  private String commerceItemId;
  private String partnerOrderId;
  private String partnerCode;
  private String submittedDate;
  private String expirationDate;
  private List<ConnectorConfirmOrderPaxRequest> paxs;
}
