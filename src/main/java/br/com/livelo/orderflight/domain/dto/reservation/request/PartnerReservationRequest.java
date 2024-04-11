package br.com.livelo.orderflight.domain.dto.reservation.request;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PartnerReservationRequest {
	private String partnerCode;
    private List<String> segmentsPartnerIds;
    private List<PartnerReservationItem> items;
    private List<PartnerReservationPax> paxs;
}
