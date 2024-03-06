package br.com.livelo.orderflight.domain.dtos.update;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalDistribuitionSystemDTO {

	@JsonAlias("provider")
	private String id;
	private String description;
	private String reservationCode;
	private String provider;
	private List<CancellationPolicyDTO> cancellationPolicies;
}
