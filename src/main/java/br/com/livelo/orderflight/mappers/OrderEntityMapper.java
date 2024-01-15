package br.com.livelo.orderflight.mappers;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

@Component
public class OrderEntityMapper {

	public ReservationResponse toCartResponse(OrderEntity orderEntity) {
		return new ReservationResponse(
				orderEntity.getCommerceOrderId(), 
				orderEntity.getPartnerOrderId(), 
				orderEntity.getPartnerCode(), 
				orderEntity.getSubmittedDate(), 
				orderEntity.getChannel(), 
				orderEntity.getTierCode(), 
				orderEntity.getOriginOrder(), 
				orderEntity.getCustomerIdentifier(), 
				orderEntity.getTransactionId(), 
				orderEntity.getExpirationDate());
	}
}
