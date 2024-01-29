package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ConnectorPartnersProxy {
    private final PartnersConfigService partnersConfigService;
    private final PartnerConnectorClient partnerConnectorClient;

    public ConnectorConfirmOrderResponse confirmOnPartner(String partnerCode,
            ConnectorConfirmOrderRequest connectorConfirmOrderRequest) {
        try {
            // TODO: como saberemos se o parceiro está ativo? lib ou confirmOrder
            // vai ser retornada uma exception da lib
            WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(),
                    Webhooks.CONFIRMATION);
            final URI connectorUri = URI.create(webhook.getConnectorUrl());
            // final URI connectorUri =
            // URI.create("https://liv-fake-http.free.beeceptor.com/http");
            return partnerConnectorClient.confirmOrder(connectorUri, connectorConfirmOrderRequest).getBody();
        } catch (FeignException exception) {
            if (exception.status() == 500) {
                ConnectorConfirmOrderStatusResponse failedStatus = ConnectorConfirmOrderStatusResponse
                        .builder()
                        .partnerCode(String.valueOf(exception.status()))
                        .code("LIVPNR-1014")
                        .partnerResponse(exception.getMessage())
                        .partnerDescription("failed")
                        .description("failed")
                        .statusDate(LocalDateTime.now())
                        .build();

                ConnectorConfirmOrderResponse connectorConfirmOrderResponse = ConnectorConfirmOrderResponse
                        .builder()
                        .partnerOrderId(connectorConfirmOrderRequest.getPartnerOrderId())
                        .partnerCode(connectorConfirmOrderRequest.getPartnerCode())
                        .submittedDate(connectorConfirmOrderRequest.getSubmittedDate())
                        .expirationDate(connectorConfirmOrderRequest.getExpirationDate())
                        .currentStatus(failedStatus)
                        .voucher(null)
                        .build();
                return connectorConfirmOrderResponse;
            }
            throw exception;
        } catch (Exception exception) {
            throw exception;
        }
    }
}
