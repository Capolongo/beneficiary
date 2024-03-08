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
	private static final String PTS = "PTS";

	private Long amount;
	private String currency = PTS;
	private String partnerCode;
	private List<ItemDTO> items;
	private List<DeliveryDTO> deliveries;

}