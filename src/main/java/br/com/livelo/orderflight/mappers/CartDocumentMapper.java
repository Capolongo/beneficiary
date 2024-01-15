package br.com.livelo.orderflight.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.ReservationDocument;
import br.com.livelo.orderflight.domain.dto.PartnerReservationDocument;

@Component
public class CartDocumentMapper {
	public List<PartnerReservationDocument> toListPartnerReservationDocument( List<ReservationDocument> listReservationDocument) {
		return listReservationDocument.stream().map(reservationDocument -> toPartnerReservationDocument(reservationDocument)).collect(Collectors.toList());
	}
	
	public PartnerReservationDocument toPartnerReservationDocument(ReservationDocument reservationDocument) {
		return PartnerReservationDocument.builder()
				.number(reservationDocument.getNumber())
				.type(reservationDocument.getType())
				.build();
	}
}
