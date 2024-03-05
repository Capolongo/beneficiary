package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import lombok.Data;

@Data
public class TrackingResponseDTO {
    private StatusDTO status;
    private String trackingDate;
}
