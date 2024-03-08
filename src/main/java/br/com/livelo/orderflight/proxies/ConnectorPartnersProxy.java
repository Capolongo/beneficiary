package br.com.livelo.orderflight.proxies;

import br.com.livelo.exceptions.WebhookException;
import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorReservationResponse;
import br.com.livelo.orderflight.exception.ConnectorReservationBusinessException;
import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import br.com.livelo.partnersconfigflightlibrary.utils.ErrorsType;
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
            log.info("ConnectorPartnersProxy.confirmOnPartner - end - id: [{}], commerceOrderId: [{}], response: [{}]", connectorConfirmOrderRequest.getId(), connectorConfirmOrderRequest.getCommerceOrderId(), connectorConfirmOrderResponse);
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
            throw handleFeignException(e, "Error on partner connector calls. httpStatus: %s ResponseBody: %s");
        } catch (WebhookException e) {
            throw handleWebhookException(e);
        } catch (Exception e) {
            throw new OrderFlightException(
                    OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR,
                    e.getMessage(),
                    "Unknown error on connector create reserve call! partner: " + request.getPartnerCode(),
                    e
            );
        }
    }

    public ConnectorReservationResponse getReservation(String id, String transactionId, String partnerCode) {
        try {
            var url = URI.create(this.partnersConfigService
                    .getPartnerWebhook(partnerCode, Webhooks.GETRESERVATION).getConnectorUrl());
            ResponseEntity<ConnectorReservationResponse> response = partnerConnectorClient.getReservation(url, id, transactionId);
            return response.getBody();
        } catch (FeignException e) {
            throw handleFeignException(e, "Error on partner get reservation connector calls. httpStatus: %s ResponseBody: %s");
        } catch (WebhookException e) {
            throw handleWebhookException(e);
        } catch (Exception e) {
            throw new OrderFlightException(
                    OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR,
                    e.getMessage(),
                    "Unknown error on connector getReservation call! partner: " + partnerCode,
                    e
            );
        }
    }

    private static OrderFlightException handleFeignException(FeignException e, String format) {
        var status = HttpStatus.valueOf(e.status());
        var message = String.format(format, e.status(), e.responseBody());

        if (status.is4xxClientError()) {
           return new ConnectorReservationBusinessException(message, e);
        }
        return new ConnectorReservationInternalException(message, e);
    }

    private static OrderFlightException handleWebhookException(WebhookException e) {
        var orderFlightErrorType = ErrorsType.UNKNOWN.equals(e.getError()) ? ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR : ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR;
        var message = String.format("Error on connector calls! error: %S", e.getError());
        return new OrderFlightException(orderFlightErrorType, e.getMessage(), message, e);
    }

    public ConnectorConfirmOrderResponse getConfirmationOnPartner(String partnerCode, String partnerOrderId, String id) throws OrderFlightException {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.GETCONFIRMATION);
            final var connectorUri = URI.create(webhook.getConnectorUrl().replace("{id}", partnerOrderId));
            log.info("ConnectorPartnersProxy.getConfirmationOnPartner - connectorUri - partnerOrderId: [{}], uri: [{}]", id, connectorUri);
            var connectorGetConfirmation = partnerConnectorClient.getConfirmation(connectorUri);
            ConnectorConfirmOrderResponse responseBody = connectorGetConfirmation.getBody();

            log.info("ConnectorPartnersProxy.getConfirmationOnPartner - success - partnerOrderId: [{}], body: [{}]", id, responseBody);
            return responseBody;
        } catch (FeignException exception) {
            log.error("ConnectorPartnersProxy.getConfirmationOnPartner exception - partnerOrderId: [{}], partnerCode: [{}], exception: [{}]", partnerOrderId, partnerCode, exception.getCause());
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR, OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR.getDescription(), null, exception);
        }
    }

    public ConnectorConfirmOrderResponse getVoucherOnPartner(String partnerCode, String partnerOrderId, String orderId) {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.VOUCHER);
            final var connectorUri = URI.create(webhook.getConnectorUrl().replace("{id}", partnerOrderId));
            var connectorGetVoucher = partnerConnectorClient.getVoucher(connectorUri);

            log.info("ConnectorPartnersProxy.getVoucherOnPartner - Partner response - body: [{}] partnerOrderId: [{}] orderId: [{}]", connectorGetVoucher.getBody(), partnerOrderId, orderId);
            return connectorGetVoucher.getBody();
        } catch (FeignException exception) {
            log.error("ConnectorPartnersProxy.getVoucherOnPartner exception - partnerOrderId: [{}], partnerCode: [{}], orderId: [{}], exception: [{}]", partnerOrderId, partnerCode, orderId, exception.getCause(), exception);
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR, OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR.getDescription(), null, exception);
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
