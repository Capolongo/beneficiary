package br.com.livelo.orderflight.domain.dtos.installment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstallmentDTO {
    private String id;

    private Integer parcels;

    private String currency;

    private BigDecimal amount;

    private BigDecimal interest;
}
