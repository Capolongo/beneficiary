package br.com.livelo.orderflight.domain.dtos.status.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateStatusItemDTO {
    private String id;
    private String commerceItemId;
    private String reason;
    private String user;
    private UpdateStatusDTO status;
}
