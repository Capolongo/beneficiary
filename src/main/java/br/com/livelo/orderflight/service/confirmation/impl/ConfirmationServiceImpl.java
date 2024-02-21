package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws OrderFlightException {
        OrderEntity order = null;
        OrderStatusEntity status = null;
        try {
            log.info("ConfirmationService.confirmOrder - Start - id: [{}]", id);
            order = orderService.getOrderById(id);

            log.info("ConfirmationService.confirmOrder - id: [{}], orderId: [{}], transactionId: [{}],  order: [{}]", id, order.getCommerceOrderId(), order.getTransactionId(), order);

            ConfirmOrderValidation.validateOrderPayload(orderRequest, order);

            var connectorConfirmOrderRequest = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(order);
            ConnectorConfirmOrderResponse connectorPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(orderRequest.getPartnerCode(), connectorConfirmOrderRequest);

            var itemFlight = orderService.getFlightFromOrderItems(order.getItems());

            orderService.updateVoucher(itemFlight, connectorPartnerConfirmation.getVoucher());
            order.setPartnerOrderId(connectorPartnerConfirmation.getPartnerOrderId());
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorPartnerConfirmation.getCurrentStatus());
        } catch (OrderFlightException exception) {
            if (!exception.getOrderFlightErrorType().equals(OrderFlightErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR)) {
                throw exception;
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getMessage()));
        } catch (Exception exception) {
            if(order != null){
                log.error("ConfirmationService.confirmOrder exception - id: [{}], orderId: [{}], transactionId: [{}],  error: [{}]", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), exception);
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getLocalizedMessage()));
        }
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
        if(order != null){
            log.info("ConfirmationService.confirmOrder - End - id: [{}], orderId: [{}], transactionId: [{}]", id, orderRequest.getCommerceOrderId(), order.getTransactionId());
        }
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }

    private ConnectorConfirmOrderStatusResponse buildStatusToFailed(String cause) {
        return ConnectorConfirmOrderStatusResponse
                .builder()
                .partnerCode(String.valueOf(500))
                .code(StatusConstants.FAILED.getCode())
                .partnerResponse(cause)
                .partnerDescription("failed")
                .description(StatusConstants.FAILED.getDescription())
                .statusDate(LocalDateTime.now())
                .build();
    }
}
