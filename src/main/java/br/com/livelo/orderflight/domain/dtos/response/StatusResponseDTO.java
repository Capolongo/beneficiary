package br.com.livelo.orderflight.domain.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponseDTO {
    public String code;
    public String description;
    public String details;
}
