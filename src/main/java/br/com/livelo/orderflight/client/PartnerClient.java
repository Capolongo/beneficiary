package br.com.livelo.orderflight.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.livelo.orderflight.domain.dto.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;

@FeignClient(name = "partner-api")
public interface PartnerClient {

	@PostMapping
	PartnerReservationResponse reservation(
			URI baseUrl,
			@RequestBody PartnerReservationRequest partnerReservationRequest,
			@RequestHeader MultiValueMap<String, String> headers);
	
}
