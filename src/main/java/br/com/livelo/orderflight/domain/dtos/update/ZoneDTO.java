package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneDTO {

	private String id;
	private String name;
	private String description;
	private CountryDTO country;
	private StateDTO state;
	private CityDTO city;
	private String latitude;
	private String longitude;
	private ZonedDateTime arrivalDate;
	private ZonedDateTime departureDate;
}
