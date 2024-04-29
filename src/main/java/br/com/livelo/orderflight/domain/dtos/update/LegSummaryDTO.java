package br.com.livelo.orderflight.domain.dtos.update;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder=true)
public class LegSummaryDTO {

	private ValidatingAirlineDTO managedAirline;
	private ValidatingAirlineDTO operationAirline;
	private Integer flightNumber;
	private Integer duration;
	private String aircraftCode;
	private DepartureArrivalSummaryDTO departure;
	private DepartureArrivalSummaryDTO arrival;
	private String seatClassCode;
	private String seatClassDescription;
	private String arrivalName;
	private String departureName;
	private List<Object> stops;
}
