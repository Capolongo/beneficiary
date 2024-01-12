package br.com.livelo.orderflight.domain.dtos.connector.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderStatusDTO {
    private Long id;
    private String code;
    private String description;
    private String partnerCode;
    private String partnerDescription;
    private String partnerResponse;
    private LocalDateTime statusDate;
}
