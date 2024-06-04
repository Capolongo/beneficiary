package br.com.livelo.orderflight.domain.dtos.orderValidate.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidateDetailDTO {

	public String message;

}