package br.com.livelo.orderflight.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.CartPax;
import br.com.livelo.orderflight.domain.dto.PartnerReservationPax;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CartPaxMapper {
	
	private final CartDocumentMapper cartDocumentMapper;
	
	public List<PartnerReservationPax> toListPartnerReservationPax( List<CartPax> listCartPax) {
		return listCartPax.stream().map(cartPax -> toPartnerReservationPax(cartPax)).collect(Collectors.toList());
	}
	
	public PartnerReservationPax toPartnerReservationPax(CartPax cartPax) {
		return PartnerReservationPax.builder()
				.type(cartPax.getType())
				.firstName(cartPax.getFirstName())
				.lastName(cartPax.getLastName())
				.gender(cartPax.getGender())
				.birthDate(cartPax.getBirthDate())
				.documents(cartDocumentMapper.toListPartnerReservationDocument(cartPax.getDocuments()))
				.email(cartPax.getEmail())
				.areaCode(cartPax.getAreaCode())
				.phone(cartPax.getPhone())
				.build();
	}
}
