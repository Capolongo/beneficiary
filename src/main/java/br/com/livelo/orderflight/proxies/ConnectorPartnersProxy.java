package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dtos.ConnectorPartnerConfirmationDTO;
import br.com.livelo.orderflight.domain.dtos.connector.ConnectorRequestDTO;
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

    public ConnectorPartnerConfirmationDTO confirmOnPartner(String partnerCode, ConnectorRequestDTO connectorRequestDTO) {
        try {
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
            final URI connectorUri = URI.create(webhook.getConnectorUrl());
            return partnerConnectorClient.confirmOrder(connectorUri, connectorRequestDTO).getBody();
        } catch (FeignException exception) {
            throw exception;
        }
    }
}
