package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PricingClient;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.utils.LogUtils;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PRICING_BUSINESS_ERROR;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PRICING_INTERNAL_ERROR;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
@AllArgsConstructor
public class PricingProxy {
    private final PricingClient pricingClient;

    public List<PricingCalculateResponse> calculate(PricingCalculateRequest request, String transactionId, String userId) {
        try {
            log.info("PricingProxy.calculate - call pricing calculate. request: {}", LogUtils.writeAsJson(request));
            ResponseEntity<List<PricingCalculateResponse>> response = pricingClient.calculate(request, transactionId, userId);
            log.info("PricingProxy.calculate -  pricing calculate response: {}", LogUtils.writeAsJson(response));

            return ofNullable(response.getBody())
                    .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_PRICING_INTERNAL_ERROR, null, "PricingProxy.calculate - body pricing is null!"));
        } catch (FeignException e) {
            var status = HttpStatus.valueOf(e.status());
            var message = String.format("PricingProxy.calculate - Error on pricing call, HttpStatus: %s Body: %s", status, e.contentUTF8());
            var errorType = status.is4xxClientError() ? ORDER_FLIGHT_PRICING_BUSINESS_ERROR : ORDER_FLIGHT_PRICING_INTERNAL_ERROR;
            throw new OrderFlightException(errorType, e.getMessage(), message, e);
        } catch (OrderFlightException e) {
            throw e;
        } catch (Exception e) {
            throw new OrderFlightException(ORDER_FLIGHT_PRICING_INTERNAL_ERROR, e.getMessage(), "PricingProxy.calculate - Unknown error on pricing call", e);
        }
    }
}
