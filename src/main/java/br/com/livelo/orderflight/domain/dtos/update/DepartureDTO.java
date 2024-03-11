package br.com.livelo.orderflight.domain.dtos.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartureDTO {

	private String date;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String location;
	private String airportName;
	private String iata;
	private Integer flightNumber;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String seatClassDescription;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer numberOfStops;
	
}
