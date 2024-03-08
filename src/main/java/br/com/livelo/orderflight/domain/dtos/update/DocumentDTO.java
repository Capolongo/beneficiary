package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
	private String doc;
	private String type;
	private String issuingCountry;
	private String expirationDate;
	private String residenceCountry;
	private String issuingDate;
}