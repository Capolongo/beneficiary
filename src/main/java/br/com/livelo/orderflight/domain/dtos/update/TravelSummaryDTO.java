package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TravelSummaryDTO {

	private TourDTO tour;
	private List<FlightSummaryDTO> flights;
	private List<Object> accommodations;
	private List<Object> vehicles;
	private List<Object> services;
//	private List<AccommodationSummaryDTO> accommodations;
//	private List<VehicleSummaryDTO> vehicles;
//	private List<ServiceSummaryDTO> services;
	private Boolean isInternational;
}
