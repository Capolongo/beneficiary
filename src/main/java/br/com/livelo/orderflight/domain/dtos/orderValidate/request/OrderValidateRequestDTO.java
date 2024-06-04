package br.com.livelo.orderflight.domain.dtos.orderValidate.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderValidateRequestDTO {

	@NotBlank
	public String id; //

	@NotNull
	public List<@Valid OrderValidateRequestItemDTO> items;
	
}