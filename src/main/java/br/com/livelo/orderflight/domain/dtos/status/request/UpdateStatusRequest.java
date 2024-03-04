package br.com.livelo.orderflight.domain.dtos.status.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateStatusRequest {
    private String orderId;
    private List<UpdateStatusItemDTO> items;
}
