package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.impl.PartnersConfigServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@AllArgsConstructor
public class ConnectorPartnersProxy {
    private final PartnersConfigServiceImpl partnersConfigService;
    private final PartnerConnectorClient partnerConnectorClient;

    public ConnectorConfirmOrderResponse confirmOnPartner(String partnerCode, ConnectorConfirmOrderRequest connectorConfirmOrderRequest) {
        try {
//            TODO: como saberemos se o parceiro está ativo? lib ou confirmOrder
//            vai ser retornada uma exception da lib
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
//            final URI connectorUri = URI.create(webhook.getConnectorUrl());
            final URI connectorUri = URI.create("https://liv-fake-http.free.beeceptor.com/http");
            return partnerConnectorClient.confirmOrder(connectorUri, connectorConfirmOrderRequest).getBody();
        } catch (Exception exception) {
            throw exception;
        }
    }
}
