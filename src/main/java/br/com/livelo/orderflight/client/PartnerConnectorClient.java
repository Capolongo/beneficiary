package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.URI;

@FeignClient(name = "partner-api", url = "localhost:3000")
public interface PartnerConnectorClient {

    @PostMapping
    ResponseEntity<PartnerReservationResponse> createReserve(
            URI baseUrl,
            @RequestBody PartnerReservationRequest partnerReservationRequest,
            @RequestHeader(value = "transactionId") String transactionId
    );
}
