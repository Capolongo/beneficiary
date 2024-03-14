package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.LiveloPartnersMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.proxies.LiveloPartnersProxy;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    @Value("${order.getConfirmationMaxProcessCountFailed}")
    private int getConfirmationMaxProcessCountFailed;

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
            orderService.updateSubmittedDate(order, connectorPartnerConfirmation.getSubmittedDate());
            order.setPartnerOrderId(connectorPartnerConfirmation.getPartnerOrderId());
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorPartnerConfirmation.getCurrentStatus());
        } catch (OrderFlightException exception) {
            if (!exception.getOrderFlightErrorType().equals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR)) {
                throw exception;
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getMessage()));
        } catch (Exception exception) {
            if (order != null) {
                log.error("ConfirmationService.confirmOrder exception - id: [{}], orderId: [{}], transactionId: [{}],  error: [{}]", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), exception);
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getLocalizedMessage()));
        }
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
        if (order != null) {
            orderService.orderDetailLog("confirmOrder", status.getCode(), order);
            log.info("ConfirmationService.confirmOrder - End - id: [{}], orderId: [{}], transactionId: [{}], statusCode: [{}], partnerCode: [{}] ", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), status.getCode(), order.getPartnerCode());
        }
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }

    public void orderProcess(OrderProcess orderProcess) {
        OrderStatusEntity status = null;

        var order = orderService.getOrderById(orderProcess.getId());
        var currentStatusCode = order.getCurrentStatus().getCode();

        if (!orderService.isSameStatus(StatusLivelo.PROCESSING.getCode(), currentStatusCode)) {
            log.warn("ConfirmationService.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var processCounter = orderService.getProcessCounter(order, Webhooks.GETCONFIRMATION.value);
        if (processCounter.getCount() >= getConfirmationMaxProcessCountFailed) {
            log.warn("ConfirmationService.orderProcess - counter exceeded limit - id: [{}]", order.getId());
            status = orderService.buildOrderStatusFailed("O contador excedeu o limite de tentativas");
        } else {
            status = processGetConfirmation(order);
        }

        if (!orderService.isSameStatus(currentStatusCode, status.getCode())) {
            Duration duration = processOrderTimeDifference(order.getCurrentStatus().getCreateDate());
            log.info("ConfirmationService.processOrderTimeDifference - process order diff time - minutes: [{}], orderId: [{}], partnerCode: [{}], oldStatus: [{}], newStatus: [{}]", duration.toMinutes(), order.getId(), order.getPartnerCode(), order.getCurrentStatus(), status);
        }

        orderService.incrementProcessCounter(processCounter);
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
        orderService.updateOrderOnLiveloPartners(order, currentStatusCode);

        log.info("ConfirmationService.orderProcess - order process counter - id: [{}], count: [{}]", order.getId(), processCounter.getCount());
        orderService.orderDetailLog("orderProcess", status.getCode(), order);
    }

    private OrderStatusEntity processGetConfirmation(OrderEntity order) {
        try {
            var connectorConfirmOrderResponse = connectorPartnersProxy.getConfirmationOnPartner(order.getPartnerCode(), order.getPartnerOrderId(), order.getId());
            var mappedStatus = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderResponse.getCurrentStatus());
            var itemFlight = orderService.getFlightFromOrderItems(order.getItems());
            orderService.updateVoucher(itemFlight, connectorConfirmOrderResponse.getVoucher());
            log.info("ConfirmationService.orderProcess - order - statusCode: [{}], partnerCode: [{}], orderId: [{}]", mappedStatus.getCode(), order.getPartnerCode(), order.getId());

            return mappedStatus;
        } catch (OrderFlightException exception) {
            return order.getCurrentStatus();
        } catch (Exception exception) {
            log.error("ConfirmationService.orderProcess - error - orderId: [{}], exception: [{}]", order.getId(), exception);
            return orderService.buildOrderStatusFailed(exception.getMessage());
        }
    }

    private Duration processOrderTimeDifference(ZonedDateTime baseTime) {
        return Duration.between(baseTime.toLocalDateTime(), LocalDateTime.now());
    }

    private ConnectorConfirmOrderStatusResponse buildStatusToFailed(String cause) {
        return ConnectorConfirmOrderStatusResponse
                .builder()
                .partnerCode(String.valueOf(500))
                .code(StatusLivelo.FAILED.getCode())
                .partnerResponse(cause)
                .partnerDescription("failed")
                .description(StatusLivelo.FAILED.getDescription())
                .statusDate(LocalDateTime.now())
                .build();
    }
}