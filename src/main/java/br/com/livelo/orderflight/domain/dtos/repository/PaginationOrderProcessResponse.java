package br.com.livelo.orderflight.domain.dtos.repository;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationOrderProcessResponse {
  private List<OrderProcess> orders;
  private int page;
  private int rows;
  private int total;
  private int totalPages;
}
