package br.com.livelo.orderflight.domain.dtos.connector.request;

import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerConfirmOrderPaxRequest {
	private String type;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthDate;
	private String email;
	private String areaCode;
	private String phoneNumber;
	private Set<PartnerConfirmDocumentRequest> documents;
}
