package br.com.livelo.orderflight.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationRequest {
	private String partnerCode;
    private List<String> segmentsPartnerIds;
    private List<PartnerReservationPax> paxs;
}
