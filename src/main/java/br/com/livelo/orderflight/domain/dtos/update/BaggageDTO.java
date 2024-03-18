package br.com.livelo.orderflight.domain.dtos.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(content=Include.NON_EMPTY)
public class BaggageDTO {
	private String type;
	
	@Builder.Default
	private Boolean isIncluded = false;
	private String uom;
	private Integer quantity;
	private Integer weight;
}
