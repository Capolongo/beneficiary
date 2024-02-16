package br.com.livelo.orderflight.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;

@FeignClient(value = "pricing-client", url = "http://api.k8s")
public interface PricingClient {
	@PostMapping
	ResponseEntity<PricingCalculateResponse[]> calculate(URI baseUrl,
			@RequestBody PricingCalculateRequest orderEntity);

}
