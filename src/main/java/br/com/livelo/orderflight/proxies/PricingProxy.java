package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PricingClient;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PRICING_BUSINESS_ERROR;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PRICING_INTERNAL_ERROR;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
@AllArgsConstructor
public class PricingProxy {
    private final PricingClient pricingClient;

    public List<PricingCalculateResponse> calculate(PricingCalculateRequest request) {
        try {
            log.info("call pricing calculate. request: {}", request);

            ResponseEntity<PricingCalculateResponse[]> response = pricingClient.calculate(
                    request);
            ofNullable(response.getBody())
                    .ifPresent(body -> log.info(" pricing calculate response: {}", body));

            return Arrays.stream(response.getBody()).toList();
        } catch (FeignException e) {
            var status = HttpStatus.valueOf(e.status());
            var message = String.format("Error on pricing call, HttpStatus: %s", status);
            var errorType = status.is4xxClientError() ? ORDER_FLIGHT_PRICING_BUSINESS_ERROR : ORDER_FLIGHT_PRICING_INTERNAL_ERROR;
            throw new OrderFlightException(errorType, e.getMessage(), message, e);
        } catch (Exception e) {
            throw new OrderFlightException(ORDER_FLIGHT_PRICING_INTERNAL_ERROR, e.getMessage(), "Unknown error on pricing call", e);
        }
    }
}
