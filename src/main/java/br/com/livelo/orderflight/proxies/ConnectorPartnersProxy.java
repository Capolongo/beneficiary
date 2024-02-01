package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component
@AllArgsConstructor
public class ConnectorPartnersProxy {
    private final PartnersConfigService partnersConfigService;
    private final PartnerConnectorClient partnerConnectorClient;
    private final ObjectMapper objectMapper;


    public ConnectorConfirmOrderResponse confirmOnPartner(String partnerCode, ConnectorConfirmOrderRequest connectorConfirmOrderRequest) throws Exception {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(),
                    Webhooks.CONFIRMATION);
            final URI connectorUri = URI.create(webhook.getConnectorUrl());
            ConnectorConfirmOrderResponse connectorConfirmOrderResponse = partnerConnectorClient.confirmOrder(connectorUri, connectorConfirmOrderRequest).getBody();

            log.info("ConnectorPartnersProxy.confirmOnPartner() - response: [{}]", connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        } catch (FeignException exception) {
            ConnectorConfirmOrderResponse connectorConfirmOrderResponse = getResponseError(exception);
            log.info("ConnectorPartnersProxy.confirmOnPartner() - exception response: [{}]", connectorConfirmOrderResponse);
            return connectorConfirmOrderResponse;
        }
    }


    private ConnectorConfirmOrderResponse getResponseError(FeignException feignException) throws Exception {
        try {
            final String content = feignException.contentUTF8();
            return objectMapper.readValue(content, ConnectorConfirmOrderResponse.class);
        } catch (Exception e) {
            throw new Exception(feignException.getMessage());
        }
    }
}
