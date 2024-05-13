package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.util.Set;

public record ReservationResponseTravelInfo (String type,
											 String reservationCode,
											 Integer adt,
											 Integer chd,
											 Integer inf,
											 String voucher,
											 String cabinClass,
											 Set<ReservationResponsePax> paxs){

}
