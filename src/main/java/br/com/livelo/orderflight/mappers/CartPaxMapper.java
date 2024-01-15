package br.com.livelo.orderflight.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.livelo.orderflight.domain.dto.ReservationPax;
import br.com.livelo.orderflight.domain.dto.PartnerReservationPax;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CartPaxMapper {
	
	private final CartDocumentMapper cartDocumentMapper;
	
	public List<PartnerReservationPax> toListPartnerReservationPax( List<ReservationPax> listReservationPaxes) {
		return listReservationPaxes.stream().map(reservationPax -> toPartnerReservationPax(reservationPax)).collect(Collectors.toList());
	}
	
	public PartnerReservationPax toPartnerReservationPax(ReservationPax reservationPax) {
		return PartnerReservationPax.builder()
				.type(reservationPax.getType())
				.firstName(reservationPax.getFirstName())
				.lastName(reservationPax.getLastName())
				.gender(reservationPax.getGender())
				.birthDate(reservationPax.getBirthDate())
				.documents(cartDocumentMapper.toListPartnerReservationDocument(reservationPax.getDocuments()))
				.email(reservationPax.getEmail())
				.areaCode(reservationPax.getAreaCode())
				.phone(reservationPax.getPhone())
				.build();
	}
}
