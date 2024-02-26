package br.com.livelo.orderflight.domain.dto.reservation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
public class ReservationRequest {
    @NotNull
    @NotBlank
    private String commerceOrderId;
    @NotNull
    private String partnerCode;
    @NotNull
    private List<String> segmentsPartnerIds;
    @NotNull
    private List<ReservationItem> items;

    @Valid
    @NotNull
    private List<ReservationPax> paxs;
}
