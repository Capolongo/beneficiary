package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PricingClient;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@AllArgsConstructor
public class PricingProxy {
    private final PricingClient pricingClient;

    public PricingCalculateResponse[] calculate(PricingCalculateRequest request) {
        try {
            log.info("call pricing calculate. request: {}", request);

            ResponseEntity<PricingCalculateResponse[]> response = pricingClient.calculate(
                    request);
            ofNullable(response.getBody())
                    .ifPresent(body -> log.info(" pricing calculate response: {}", body));

            return response.getBody();
        } catch (FeignException e) {
            var status = HttpStatus.valueOf(e.status());
            log.error("Error on pricing call, HttpStatus: {}", e,status);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        } catch (Exception e) {
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }
}
