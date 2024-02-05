package br.com.livelo.orderflight.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;

@FeignClient(name = "partner-api", url = "localhost:3000")
public interface PartnerConnectorClient {

	@PostMapping
	ResponseEntity<PartnerReservationResponse> createReserve(
			URI baseUrl,
			@RequestBody PartnerReservationRequest partnerReservationRequest,
			@RequestHeader MultiValueMap<String, String> headers
	);
}
