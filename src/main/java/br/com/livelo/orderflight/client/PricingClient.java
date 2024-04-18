package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.constants.HeadersConstants;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(value = "pricing-client", url = "${client.pricingcalculatorflight.endpoint}")
public interface PricingClient {
    @PostMapping
    ResponseEntity<List<PricingCalculateResponse>> calculate(
        @RequestBody PricingCalculateRequest orderEntity,
        @RequestHeader(value = HeadersConstants.LIVELO_TRANSACTION_ID_HEADER) String transactionId,
        @RequestHeader(value = HeadersConstants.LIVELO_USER_ID_HEADER) String userId
        );

}
