package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AmountDTO {
    private BigDecimal pointsAmount;
}
