package br.com.livelo.orderflight.proxies;

import br.com.livelo.exceptions.WebhookException;
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
import br.com.livelo.partnersconfigflightlibrary.utils.ErrorsType;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR;
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
            log.info("ConnectorPartnersProxy.confirmOnPartner - start - id: [{}], commerceOrderId: [{}], partnerCode: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(), partnerCode);
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
            final var connectorUri = URI.create(webhook.getConnectorUrl());

            ResponseEntity<ConnectorConfirmOrderResponse> response = partnerConnectorClient.confirmOrder(connectorUri, connectorConfirmOrderRequest);
            var connectorConfirmOrderResponse = response.getBody();
            log.info("ConnectorPartnersProxy.confirmOnPartner - end - id: [{}], commerceOrderId: [{}], response: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(),  connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        } catch (FeignException exception) {
            var connectorConfirmOrderResponse = getResponseError(exception, connectorConfirmOrderRequest);
            log.warn("ConnectorPartnersProxy.confirmOnPartner exception - id: [{}], commerceOrderId: [{}], partnerCode: [{}], exception response: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(), partnerCode, connectorConfirmOrderResponse);
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
            var message = String.format("Error on partner connector calls. httpStatus: %s ResponseBody: %s", e.status(), e.responseBody());

            if (status.is4xxClientError()) {
                throw new ConnectorReservationBusinessException(message, e);
            }
            throw new ConnectorReservationInternalException(message, e);
        } catch (WebhookException e) {
            var orderFlightErrorType = ErrorsType.UNKNOWN.equals(e.getError()) ? ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR : ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR;
            var message = String.format("Error on connector calls! error: %S", e.getError());
            throw new OrderFlightException(orderFlightErrorType, e.getMessage(), message, e);
        } catch (Exception e) {
            log.error("Unknown error on connector call ", e);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }

    private ConnectorConfirmOrderResponse getResponseError(FeignException feignException, ConnectorConfirmOrderRequest connectorConfirmOrderRequest) throws OrderFlightException {
        final String content = feignException.contentUTF8();
        try {
            log.info("ConnectorPartnersProxy.getResponseError() - contentUTF8: [{}]", content);
            var connectorConfirmOrderResponse = objectMapper.readValue(content, ConnectorConfirmOrderResponse.class);
            if (connectorConfirmOrderResponse.getCurrentStatus() == null) {
                log.error("ConnectorPartnersProxy.getResponseError - ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR - id: [{}], commerceOrderId: [{}], contentUTF8: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(), content);
                throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR, content, null);
            }

            return connectorConfirmOrderResponse;
        } catch (Exception e) {
            log.error("ConnectorPartnersProxy.getResponseError - ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR - id: [{}], commerceOrderId: [{}], contentUTF8: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(), content);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR, content, null, e);
        }
    }
}
