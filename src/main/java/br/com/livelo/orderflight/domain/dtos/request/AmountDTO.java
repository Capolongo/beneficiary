package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AmountDTO {
    private BigDecimal pointsAmount;
}
