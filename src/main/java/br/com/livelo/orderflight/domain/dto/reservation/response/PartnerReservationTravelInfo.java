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
    private Integer adt;
    private Integer chd;
    private Integer inf;
    private String cabinClass;
    private String voucher;
    private Boolean isInternational;
}
