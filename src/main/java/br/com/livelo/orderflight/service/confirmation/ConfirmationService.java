package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.utils.PayloadComparison;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationService {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest order) throws Exception {
        OrderEntity foundOrder = orderService.getOrderById(id);

        validateIfAlreadyIsConfirmed(foundOrder.getCurrentStatus(), order.getResubmission());
        validateRequestPayload(order, foundOrder);

        ConnectorConfirmOrderResponse connectorPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(
                order.getPartnerCode(),
                confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(foundOrder)
        );

        validatePartnerOrderIds(foundOrder.getPartnerOrderId(), connectorPartnerConfirmation.getPartnerOrderId());
        OrderEntity updatedOrder = updateOrderStatus(foundOrder, connectorPartnerConfirmation.getCurrentStatus());
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(updatedOrder);
    }

    private void validateRequestPayload(ConfirmOrderRequest order, OrderEntity foundOrder) throws Exception {
        List<Boolean> validationList = List.of(
                order.getCommerceOrderId().equals(foundOrder.getCommerceOrderId()),
                order.getPrice().getPointsAmount().equals(foundOrder.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(order.getItems(), foundOrder.getItems()));

        if (validationList.contains(false)) {
            throw new Exception("Objects are not equal");
        }
    }

    private void validateIfAlreadyIsConfirmed(OrderStatusEntity orderStatus, Boolean resubmission) throws Exception {
        if (orderStatus.getCode().equals("LIVPNR-1006") || resubmission) {
            return;
        }

        throw new Exception("Order is already confirmed");
    }

    private void validatePartnerOrderIds(String foundOrderId, String partnerConnectorOrderId) throws Exception {
        if (!partnerConnectorOrderId.equals(foundOrderId)) {
            throw new Exception("partnerOrderIds are different");
        }
    }

    private OrderEntity updateOrderStatus(OrderEntity order, ConnectorConfirmOrderStatusResponse status) {
        order.getStatusHistory().add(
                confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(status));
        order.setCurrentStatus(
                confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(status));
        return orderRepository.save(order);
    }
}
