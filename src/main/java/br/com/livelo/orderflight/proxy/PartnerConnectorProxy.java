package br.com.livelo.orderflight.proxy;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerConnectorProxy {
    private final PartnerConnectorClient partnerConnectorClient;
    private final PartnersConfigService partnersConfigService;

    @Retryable(retryFor = ConnectorReservationInternalException.class, maxAttempts = 1)
    public PartnerReservationResponse createReserve(PartnerReservationRequest request, String transactionId) {
        try {
            var url = this.getUrlByPartnerCode(request.getPartnerCode());
            log.info("call connector partner create reserve. partner: {} url: {} request: {}", request.getPartnerCode(), url, request);

            var response = partnerConnectorClient.createReserve(
                    url,
                    request,
                    transactionId);
            ofNullable(response.getBody()).ifPresent(body -> log.info("create reserve partner connector response: {}", body));

            return response.getBody();
        } catch (ReservationException e) {
            throw e;
        } catch (FeignException e) {
            log.error("Error on connector call ", e);
            var status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                var message = String.format("Internal error on partner connector calls. httpStatus: %s ResponseBody: %s", e.status(), e.responseBody());
                throw new ConnectorReservationInternalException(message, e);
            } else {
                var message = String.format("Business error on partner connector calls. httpStatus: %s ResponseBody: %s ", e.status(), e.responseBody().toString());
                throw new ConnectorReservationBusinessException(message, e);
            }
        } catch (Exception e) {
            log.error("Unknown error on connector call ", e);
            throw new ReservationException(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }

    private URI getUrlByPartnerCode(String partnerCode) {
        return URI.create(this.partnersConfigService.getPartnerWebhook(partnerCode, Webhooks.RESERVATION).getConnectorUrl());
    }
}
