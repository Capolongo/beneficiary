package br.com.livelo.orderflight.domain.dtos.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProcess {
    private String id;
    private String commerceOrderId;
}
