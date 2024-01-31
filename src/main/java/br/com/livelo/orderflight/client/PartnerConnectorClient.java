package br.com.livelo.orderflight.client;

import java.net.URI;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.util.MultiValueMap;

@FeignClient(value = "partner-client", url = "http://test")
public interface PartnerConnectorClient {
	@PostMapping
	ResponseEntity<ConnectorConfirmOrderResponse> confirmOrder(URI baseUrl,
			@RequestBody ConnectorConfirmOrderRequest ConnectorConfirmOrderRequest);

	@PostMapping
	ResponseEntity<PartnerReservationResponse> reservation(
			URI baseUrl,
			@RequestBody PartnerReservationRequest partnerReservationRequest,
			@RequestHeader MultiValueMap<String, String> headers);
}
