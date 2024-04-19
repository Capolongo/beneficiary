package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDTO {

	private String id;
	private String description;
	private List<OriginDTO> origins;
	private List<DestinationDTO> destinations;
}
