package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.net.URI;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@AllArgsConstructor
public class ConnectorPartnersProxy {
    private final PartnersConfigService partnersConfigService;
    private final PartnerConnectorClient partnerConnectorClient;
    private final ObjectMapper objectMapper;

    public ConnectorConfirmOrderResponse confirmOnPartner(String partnerCode,
            ConnectorConfirmOrderRequest connectorConfirmOrderRequest) throws Exception {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(),
                    Webhooks.CONFIRMATION);
            final URI connectorUri = URI.create(webhook.getConnectorUrl());
            ConnectorConfirmOrderResponse connectorConfirmOrderResponse = partnerConnectorClient
                    .confirmOrder(connectorUri, connectorConfirmOrderRequest).getBody();

            log.info("ConnectorPartnersProxy.confirmOnPartner() - response: [{}]", connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        } catch (FeignException exception) {
            ConnectorConfirmOrderResponse connectorConfirmOrderResponse = getResponseError(exception);
            log.info("ConnectorPartnersProxy.confirmOnPartner() - exception response: [{}]",
                    connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        }
    }

    @Retryable(retryFor = ConnectorReservationInternalException.class, maxAttempts = 1)
    public PartnerReservationResponse createReserve(PartnerReservationRequest request, String transactionId) {
        try {
            URI url = URI.create(this.partnersConfigService
                    .getPartnerWebhook(request.getPartnerCode(), Webhooks.RESERVATION).getConnectorUrl());
            log.info("call connector partner create reserve. partner: {} url: {} request: {}", request.getPartnerCode(),
                    url, request);

            ResponseEntity<PartnerReservationResponse> response = partnerConnectorClient.createReserve(
                    url,
                    request,
                    transactionId);
            ofNullable(response.getBody())
                    .ifPresent(body -> log.info("create reserve partner connector response: {}", body));

            return response.getBody();
        } catch (OrderFlightException e) {
            throw e;
        } catch (FeignException e) {
            log.error("Error on connector call ", e);
            HttpStatus status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                String message = String.format(
                        "Internal error on partner connector calls. httpStatus: %s ResponseBody: %s", e.status(),
                        e.responseBody());
                throw new ConnectorReservationInternalException(message, e);
            } else {
                String message = String.format(
                        "Business error on partner connector calls. httpStatus: %s ResponseBody: %s ", e.status(),
                        e.responseBody().toString());
                throw new ConnectorReservationBusinessException(message, e);
            }
        } catch (Exception e) {
            log.error("Unknown error on connector call ", e);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }

    private ConnectorConfirmOrderResponse getResponseError(FeignException feignException) throws OrderFlightException {
        try {
            final String content = feignException.contentUTF8();
            return objectMapper.readValue(content, ConnectorConfirmOrderResponse.class);
        } catch (Exception e) {
            throw new OrderFlightException(OrderFlightErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, e.getMessage(), null,
                    e);
        }
    }

}
