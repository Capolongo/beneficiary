package br.com.livelo.orderflight.domain.dtos.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProcess {
  private String id;
  private String commerceOrderId;
}
