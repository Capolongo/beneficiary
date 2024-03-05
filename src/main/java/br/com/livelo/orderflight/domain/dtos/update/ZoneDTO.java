package br.com.livelo.orderflight.domain.dtos.update;

import br.com.livelo.partners.dto.orders.response.partnerinfo.reservation.tour.origin.country.CountryDTO;
import br.com.livelo.partners.dto.orders.response.partnerinfo.reservation.tour.origin.zone.CityDTO;
import br.com.livelo.partners.dto.orders.response.partnerinfo.reservation.tour.origin.zone.StateDTO;
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
