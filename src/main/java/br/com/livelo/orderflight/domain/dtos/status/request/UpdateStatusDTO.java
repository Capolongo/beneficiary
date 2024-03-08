package br.com.livelo.orderflight.domain.dtos.status.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateStatusDTO {
    private String code;
    private String message;
    private String details;
}
