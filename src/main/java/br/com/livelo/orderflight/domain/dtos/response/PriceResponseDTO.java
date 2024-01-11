package br.com.livelo.orderflight.domain.dtos.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponseDTO {
    public BigDecimal amount;
    public BigDecimal pointsAmount;
}
