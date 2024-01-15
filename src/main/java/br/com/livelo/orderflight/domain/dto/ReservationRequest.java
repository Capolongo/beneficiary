package br.com.livelo.orderflight.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
public class ReservationRequest {
    @NotNull
    private String commerceOrderId;
    @NotNull
    private String partnerOrderId;
    @NotNull
    private String partnerCode;
    @NotNull
    private List<String> segmentsPartnerIds;
    @NotNull
    private List<ReservationItem> items;
    @NotNull
    private List<ReservationPax> paxs;
}
