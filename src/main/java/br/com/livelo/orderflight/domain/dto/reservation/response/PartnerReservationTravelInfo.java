package br.com.livelo.orderflight.domain.dto.reservation.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartnerReservationTravelInfo {
	private String type;
    private String reservationCode;
    private Integer adultQuantity;
    private Integer childQuantity;
    private Integer babyQuantity;
    private String typeClass;
    private String voucher;
    private Boolean isInternational;
}
