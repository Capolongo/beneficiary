package br.com.livelo.orderflight.domain.mappers;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

@Component
public class OrderEntityMapper {

	public CartResponse toCartResponse(OrderEntity orderEntity) {
		return new CartResponse(
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
