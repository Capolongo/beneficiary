package br.com.livelo.orderflight.service.checkout;

import br.com.livelo.orderflight.client.PartnerClient;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;

import java.net.URI;

import org.springframework.stereotype.Service;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final PartnerClient partnerClient;
    private final OrderService orderService;
    private final PartnersConfigService partnersConfigService;

    public OrderEntity confirmOrder(String id) throws Exception {
        OrderEntity order = orderService.getOrderById(id); // Base
        validateRequest(order);

        confirmOnPartner(order.getPartnerCode());
        // salva informacoes no OrderService
        // retorna resultado
        return order;
    }

    private void validateRequest(OrderEntity orderBase) throws Exception {
        // if (false) {
        // throw new Exception("Objects not equal");
        // }
    }

    private void confirmOnPartner(String partnerCode) {

        WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode, Webhooks.CONFIRMATION);
        URI url = URI.create(webhook.getConnectorUrl());
        partnerClient.partnerConnectorUrl(url);
        // chama proxy de confirmação
        // retorna resultado por meio do status
    }
}
