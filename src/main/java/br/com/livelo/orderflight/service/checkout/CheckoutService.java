package br.com.livelo.orderflight.service.checkout;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dtos.ConnectorPartnerConfirmationDTO;
import br.com.livelo.orderflight.domain.dtos.connector.ConnectorRequestDTO;
import br.com.livelo.orderflight.domain.dtos.request.ConfirmRequestDTO;
import br.com.livelo.orderflight.domain.dtos.response.ConfirmResponseDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.utils.PayloadComparison;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Service;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final PartnerConnectorClient partnerConnectorClient;
    private final OrderService orderService;
    private final PartnersConfigService partnersConfigService;
    private final OrderRepository orderRepository;
    private final ConfirmOrderMapper confirmOrderMapper;

    public ConfirmResponseDTO confirmOrder(String id, ConfirmRequestDTO order) throws Exception {
        OrderEntity foundOrder = orderService.getOrderById(id);
        validateRequest(order, foundOrder);

        ConnectorPartnerConfirmationDTO connectorPartnerConfirmation = confirmOnPartner(order.getPartnerCode(),
                confirmOrderMapper.orderEntityToConnectorRequestDTO(foundOrder));

        if (!connectorPartnerConfirmation.getPartnerOrderId().equals(foundOrder.getPartnerOrderId())) {
            throw new Exception("partnerOrderIds are different");
        }

        foundOrder.getStatusHistory().add(connectorPartnerConfirmation.getCurrentStatus());
        foundOrder.setCurrentStatus(connectorPartnerConfirmation.getCurrentStatus());

        OrderEntity updatedOrder = orderRepository.save(foundOrder);
        ConfirmResponseDTO confirmResponseDTO = confirmOrderMapper.entityToResponseDTO(updatedOrder);

        return confirmResponseDTO;
    }

    private void validateRequest(ConfirmRequestDTO order, OrderEntity foundOrder) throws Exception {
        List<Boolean> validationList = List.of(
                order.getCommerceOrderId().equals(foundOrder.getCommerceOrderId()),
                order.getAmount().getPointsAmount().equals(foundOrder.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(order.getItems(), foundOrder.getItems()));

        if (validationList.contains(false)) {
            throw new Exception("Objects are not equal");
        }
    }

    private ConnectorPartnerConfirmationDTO confirmOnPartner(String partnerCode,
            ConnectorRequestDTO connectorRequestDTO) {
        WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode.toUpperCase(), Webhooks.CONFIRMATION);
        URI url = URI.create(webhook.getConnectorUrl().replace("{id}/", ""));

        return partnerConnectorClient.partnerConnectorConfirm(url, connectorRequestDTO).getBody();
        // chama proxy de confirmação
        // retorna resultado por meio do status
    }
}
