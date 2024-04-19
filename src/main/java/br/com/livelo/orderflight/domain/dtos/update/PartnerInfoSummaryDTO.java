package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PartnerInfoSummaryDTO {

	TravelSummaryDTO travel;
}
