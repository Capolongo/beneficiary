package br.com.livelo.orderflight.domain.dtos.connector;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectorRequestDTO {
  private String id;
  private String commerceOrderId;
  private String commerceItemId;
  private String partnerOrderId;
  private String partnerCode;
  private String submittedDate;
  private String expirationDate;
  // private Set<String> segmentsPartnerIds;
  private List<PaxsConnectorRequestDTO> paxs;
}
