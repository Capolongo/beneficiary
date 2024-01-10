package br.com.livelo.orderflight.domain.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponseDTO {
    public double amount;
    public int pointsAmount;
}
