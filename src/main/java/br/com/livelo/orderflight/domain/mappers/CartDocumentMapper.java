package br.com.livelo.orderflight.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.CartDocument;
import br.com.livelo.orderflight.domain.dto.PartnerReservationDocument;

@Component
public class CartDocumentMapper {
	public List<PartnerReservationDocument> toListPartnerReservationDocument( List<CartDocument> listCartDocument) {
		return listCartDocument.stream().map(cartDocument -> toPartnerReservationDocument(cartDocument)).collect(Collectors.toList());
	}
	
	public PartnerReservationDocument toPartnerReservationDocument(CartDocument cartDocument) {
		return PartnerReservationDocument.builder()
				.number(cartDocument.getNumber())
				.type(cartDocument.getType())
				.build();
	}
}
