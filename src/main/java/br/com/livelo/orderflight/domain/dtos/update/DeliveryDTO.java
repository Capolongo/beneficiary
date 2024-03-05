package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

	public String deliveryTracking;
	public String deliveryDate;
	public List<ItemDTO> items;
	public List<TrackingDTO> trackingDTOs;

}