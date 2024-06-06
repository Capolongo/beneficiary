package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.PartnerConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@FeignClient(value = "partner-client", url = "http://api.k8s")
public interface PartnerConnectorClient {
    @PostMapping
    ResponseEntity<PartnerConfirmOrderResponse> confirmOrder(URI baseUrl,
                                                             @RequestBody PartnerConfirmOrderRequest connectorConfirmOrderRequest,
                                                             @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, required = false) String transactionId,
                                                             @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER, required = false) String userId
    );

    @PostMapping
    ResponseEntity<PartnerReservationResponse> createReserve(
            URI baseUrl,
            @RequestBody PartnerReservationRequest partnerReservationRequest,
            @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER) String transactionId,
            @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER, required = false) String userId);

    @GetMapping("{id}")
    ResponseEntity<PartnerReservationResponse> getReservation(
            URI baseUri,
            @PathVariable("id") String id,
            @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, required = false) String transactionId,
            @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER, required = false) String userId
    );

    @GetMapping
    ResponseEntity<PartnerConfirmOrderResponse> getConfirmation(URI baseUrl);

    @GetMapping
    ResponseEntity<PartnerConfirmOrderResponse> getVoucher(URI baseUrl);
}
