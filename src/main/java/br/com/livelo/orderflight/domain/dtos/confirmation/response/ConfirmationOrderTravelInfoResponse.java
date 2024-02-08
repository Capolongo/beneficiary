package br.com.livelo.orderflight.domain.dtos.confirmation.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ConfirmationOrderTravelInfoResponse {
    private String type;
    private String reservationCode;
    private int adultQuantity;
    private int childQuantity;
    private int babyQuantity;
    private String typeClass;
    private String voucher;
    private Set<ConfirmationOrderPaxResponse> paxs;
}
