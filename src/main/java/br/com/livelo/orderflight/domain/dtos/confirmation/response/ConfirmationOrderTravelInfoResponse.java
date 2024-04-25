package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmationOrderTravelInfoResponse {
    private String type;
    private String reservationCode;
    private int adt;
    private int chd;
    private int inf;
    private String cabinClass;
    private String voucher;
    private Set<ConfirmationOrderPaxResponse> paxs;
}
