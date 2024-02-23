package br.com.livelo.orderflight.client;

import java.net.URI;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "partner-client", url = "http://api.k8s")
public interface PartnerConnectorClient {
	@PostMapping
	ResponseEntity<ConnectorConfirmOrderResponse> confirmOrder(URI baseUrl,
			@RequestBody ConnectorConfirmOrderRequest connectorConfirmOrderRequest);

	@PostMapping
	ResponseEntity<PartnerReservationResponse> createReserve(
			URI baseUrl,
			@RequestBody PartnerReservationRequest partnerReservationRequest,
			@RequestHeader(value = "transactionId") String transactionId);

	@GetMapping("/v1/order/{id}/confirmation")
	ResponseEntity<ConnectorConfirmOrderResponse> getConfirmation(URI baseUrl);
}
