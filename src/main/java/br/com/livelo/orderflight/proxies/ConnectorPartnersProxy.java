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

    public ConnectorConfirmOrderResponse confirmOnPartner(String partnerCode, ConnectorConfirmOrderRequest connectorConfirmOrderRequest) throws OrderFlightException {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
            final var connectorUri = URI.create(webhook.getConnectorUrl());

            ResponseEntity<ConnectorConfirmOrderResponse> response = partnerConnectorClient.confirmOrder(connectorUri, connectorConfirmOrderRequest);
            var connectorConfirmOrderResponse = response.getBody();
            log.info("ConnectorPartnersProxy.confirmOnPartner() - requestId: [{}], commerceOrderId: [{}], response: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(),  connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        } catch (FeignException exception) {
            var connectorConfirmOrderResponse = getResponseError(exception);
            log.info("ConnectorPartnersProxy.confirmOnPartner() - exception response: [{}]",
                    connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        }
    }

    @Retryable(retryFor = ConnectorReservationInternalException.class, maxAttempts = 1)
    public PartnerReservationResponse createReserve(PartnerReservationRequest request, String transactionId) {
        try {
            var url = URI.create(this.partnersConfigService
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
            var status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                var message = String.format(
                        "Internal error on partner connector calls. httpStatus: %s ResponseBody: %s", e.status(),
                        e.responseBody());
                throw new ConnectorReservationInternalException(message, e);
            } else {
                var message = String.format(
                        "Business error on partner connector calls. httpStatus: %s ResponseBody: %s ", e.status(),
                        e.responseBody().toString());
                throw new ConnectorReservationBusinessException(message, e);
            }
        } catch (Exception e) {
            log.error("Unknown error on connector call ", e);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }

    public ConnectorConfirmOrderResponse getConfirmationOnPartner(String partnerCode, String id) {
        WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.GETCONFIRMATION);
//        final var connectorUri = URI.create(webhook.getConnectorUrl().replace("{id}", id));
        final var connectorUri = URI.create(webhook.getConnectorUrl().replace("{id}", id + "001030"));
        var connectorGetConfirmation = partnerConnectorClient.getConfirmation(connectorUri);

        return connectorGetConfirmation.getBody();
    }

    private ConnectorConfirmOrderResponse getResponseError(FeignException feignException) throws OrderFlightException {
        final String content = feignException.contentUTF8();
        try {
            log.info("ConnectorPartnersProxy.getResponseError() - contentUTF8: [{}]", content);
            var connectorConfirmOrderResponse = objectMapper.readValue(content, ConnectorConfirmOrderResponse.class);
            if (connectorConfirmOrderResponse.getCurrentStatus() == null) {
                throw new OrderFlightException(OrderFlightErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, content, null);
            }

            return connectorConfirmOrderResponse;
        } catch (Exception e) {
            throw new OrderFlightException(OrderFlightErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, content, null, e);
        }
    }
}
