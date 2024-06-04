package br.com.livelo.orderflight.domain.dtos.orderValidate.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidateResponseDTO {

	public String id;
	public String status;
	public List<OrderValidateItemDTO> items;

}