package br.com.livelo.orderflight.domain.dtos.orderValidate.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderValidateRequestItemDTO {

	@NotBlank
	public String id;

	@NotBlank
	public String commerceItemId;

}