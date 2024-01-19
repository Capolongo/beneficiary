package br.com.livelo.orderflight.domain.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationPax {
	private String type;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthDate;
	private String email;
	private String areaCode;
	private String phone;
	private List<PartnerReservationDocument> documents;
}
