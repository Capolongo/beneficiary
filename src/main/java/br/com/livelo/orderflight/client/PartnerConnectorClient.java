package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@FeignClient(value = "partner-client", url = "http://api.k8s")
public interface PartnerConnectorClient {
    @PostMapping
    ResponseEntity<ConnectorConfirmOrderResponse> confirmOrder(URI baseUrl,
                                                               @RequestBody ConnectorConfirmOrderRequest connectorConfirmOrderRequest,
                                                               @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER) String transactionId,
                                                               @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER) String userId
                                                               );

    @PostMapping
    ResponseEntity<PartnerReservationResponse> createReserve(
            URI baseUrl,
            @RequestBody PartnerReservationRequest partnerReservationRequest,
            @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER) String transactionId,
            @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER) String userId);

    @GetMapping
    ResponseEntity<PartnerReservationResponse> getReservation(
            URI baseUri,
            @RequestParam(value = "id") String id,
            @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER) String transactionId,
            @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER) String userId,
            @RequestHeader(value = "segmentsPartnerIds") List<String> segmentsPartnerIds
    );

    @GetMapping
    ResponseEntity<ConnectorConfirmOrderResponse> getConfirmation(URI baseUrl);

    @GetMapping
    ResponseEntity<ConnectorConfirmOrderResponse> getVoucher(URI baseUrl);
}
