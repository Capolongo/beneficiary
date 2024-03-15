package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDTO {

	private String departureDate;
	private String arrivalDate;
	private ZoneDTO zone;
}
