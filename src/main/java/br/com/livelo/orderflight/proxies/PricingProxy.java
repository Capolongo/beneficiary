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
        } catch (OrderFlightException e) {
            throw e;
        } catch (FeignException e) {
            log.error("Error on pricing call ", e);
            var status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                var message = String.format(
                        "Internal error on pricing calls. httpStatus: %s ResponseBody: %s", e.status(),
                        e.responseBody());
                throw new ConnectorReservationInternalException(message, e);
            } else {
                var message = String.format(
                        "Business error on pricing calls. httpStatus: %s ResponseBody: %s ", e.status(),
                        e.responseBody().toString());
                throw new ConnectorReservationBusinessException(message, e);
            }
        } catch (Exception e) {
            log.error("Unknown error on pricing call ", e);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }
}
