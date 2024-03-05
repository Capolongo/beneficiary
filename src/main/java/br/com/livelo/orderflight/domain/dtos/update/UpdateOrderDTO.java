package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {

	public Long amount;
	public String currency;
	public String partnerCode;
	public List<ItemDTO> items;
	public List<DeliveryDTO> deliveries;

}