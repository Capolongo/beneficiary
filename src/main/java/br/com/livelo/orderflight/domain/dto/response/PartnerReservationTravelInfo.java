package br.com.livelo.orderflight.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationTravelInfo {
	private String type;
    private String reservationCode;
    private Integer adultQuantity;
    private Integer childQuantity;
    private Integer babyQuantity;
    private String typeClass;
    private String voucher;
}
