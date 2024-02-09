package br.com.livelo.orderflight.domain.dtos.repository;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationOrderProcessResponse {
  List<OrderProcess> orders;
  int page;
  int rows;
  int total;
  int totalPages;
}
