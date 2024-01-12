package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.utils.PayloadComparison;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        validateIfAlreadyIsConfirmed(foundOrder.getStatusHistory());
        validateRequestPayload(order, foundOrder);

        ConnectorConfirmOrderResponse connectorPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(order.getPartnerCode(),
                confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(foundOrder));

        validatePartnerOrderIds(foundOrder.getPartnerOrderId(), connectorPartnerConfirmation.getPartnerOrderId());

        foundOrder.getStatusHistory().add(
                confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(connectorPartnerConfirmation.getCurrentStatus())
        );
        foundOrder.setCurrentStatus(
                confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(connectorPartnerConfirmation.getCurrentStatus())
        );
        OrderEntity updatedOrder = orderRepository.save(foundOrder);

        return confirmOrderMapper.orderEntityToConfirmOrderResponse(updatedOrder);
    }

    private void validateRequestPayload(ConfirmOrderRequest order, OrderEntity foundOrder) throws Exception {
        List<Boolean> validationList = List.of(
                order.getCommerceOrderId().equals(foundOrder.getCommerceOrderId()),
                order.getAmount().getPointsAmount().equals(foundOrder.getPrice().getPointsAmount()),
                PayloadComparison.compareItems(order.getItems(), foundOrder.getItems()));

        if (validationList.contains(false)) {
            throw new Exception("Objects are not equal");
        }
    }

    private void validateIfAlreadyIsConfirmed(Set<OrderStatusEntity> orderStatus) throws Exception {
        Optional<OrderStatusEntity> confirmedStatus = orderStatus.stream().filter(status -> "LIVPNR-1007".equals(status.getCode())).findFirst();

        if (confirmedStatus.isPresent()) {
            throw new Exception("Order is already confirmed");
        }
    }

    private void validatePartnerOrderIds(String foundOrderId, String partnerConnectorOrderId) throws Exception {
        if (!partnerConnectorOrderId.equals(foundOrderId)) {
            throw new Exception("partnerOrderIds are different");
        }
    }
}
