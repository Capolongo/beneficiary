package br.com.livelo.orderflight.domain.dtos.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AmountDTO {
    public int pointsAmount;
}
