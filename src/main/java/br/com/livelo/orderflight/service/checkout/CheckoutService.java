package br.com.livelo.orderflight.service.checkout;

import br.com.livelo.orderflight.client.PartnerClient;
import br.com.livelo.orderflight.domain.dtos.ConnectorPartnerConfirmationDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.utils.PayloadComparison;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final PartnerClient partnerClient;
    private final OrderService orderService;
    private final PartnersConfigService partnersConfigService;
    private final OrderRepository orderRepository;

    public OrderEntity confirmOrder(String id, OrderEntity order) throws Exception {
        OrderEntity foundOrder = orderService.getOrderById(id);
//        validateRequest(order, foundOrder);


        ConnectorPartnerConfirmationDTO connectorPartnerConfirmation = confirmOnPartner(order.getPartnerCode(), order.getPartnerOrderId());

        if (!connectorPartnerConfirmation.getPartnerOrderId().equals(foundOrder.getPartnerOrderId())) {
            throw new Exception("hello");
        }

        foundOrder.getStatusHistory().add(connectorPartnerConfirmation.getCurrentStatus());
        foundOrder.setCurrentStatus(connectorPartnerConfirmation.getCurrentStatus());

        return orderRepository.save(foundOrder);
        // salva informacoes no OrderService
        // retorna resultado
    }

    private void validateRequest(OrderEntity order, OrderEntity foundOrder) throws Exception {
        boolean validationList = List.of(
                order.getCommerceOrderId().equals(foundOrder.getCommerceOrderId()),
                order.getPrice().getAmount().equals(foundOrder.getPrice().getAmount()),
                PayloadComparison.compareItems(order.getItems(), foundOrder.getItems())
        ).contains(false);

        if (validationList) {
            throw new Exception("Objects are not equal");
        }
    }

//    private ConnectorPartnerConfirmationDTO confirmOnPartner(String partnerCode, String orderId) {
    private ConnectorPartnerConfirmationDTO confirmOnPartner(String partnerCode, String orderId) {
        WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
        URI url = URI.create(webhook.getConnectorUrl().replace("{id}", orderId));

        return partnerClient.partnerConnectorUrl(url);
        // chama proxy de confirmação
        // retorna resultado por meio do status
    }
}
