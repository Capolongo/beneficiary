package br.com.livelo.orderflight.domain.dtos.connector;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaxsConnectorRequestDTO {
	private String type;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthDate;
	private String document;
	private String documentType;
	private String email;
	private String areaCode;
	private String phone;
}