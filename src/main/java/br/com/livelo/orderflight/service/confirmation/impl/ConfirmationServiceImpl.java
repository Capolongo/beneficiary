package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.PartnerConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.headers.RequiredHeaders;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import br.com.livelo.orderflight.utils.DynatraceUtils;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    @Value("${order.getConfirmationMaxProcessCountFailed}")
    private int getConfirmationMaxProcessCountFailed;

    public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest, RequiredHeaders headers) throws OrderFlightException {
        OrderEntity order = null;
        OrderCurrentStatusEntity status = null;

        try {
            log.info("ConfirmationService.confirmOrder - Start - id: [{}]", id);
            order = orderService.getOrderById(id);

            if(headers.getUserId().isEmpty()) {
                headers.setUserId(orderRequest.getCustomerId());
            }

            if(headers.getTransactionId().isEmpty()) {
                headers.setTransactionId(order.getTransactionId());
            }

            log.info("ConfirmationService.confirmOrder - id: [{}], orderId: [{}], transactionId: [{}],  order: [{}], headers: [{}]", id, order.getCommerceOrderId(), order.getTransactionId(), order, headers);

            ConfirmOrderValidation.validateOrderPayload(orderRequest, order);

            PartnerConfirmOrderResponse partnerPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(orderRequest.getPartnerCode(), order, headers);

            var itemFlight = orderService.getFlightFromOrderItems(order.getItems());

            orderService.updateVoucher(itemFlight, partnerPartnerConfirmation.getVoucher());
            orderService.updateSubmittedDate(order, orderRequest.getSubmittedDate());
            order.setPartnerOrderId(partnerPartnerConfirmation.getPartnerOrderId());
            order.setChannel(orderRequest.getChannel());
            order.setOriginOrder(orderRequest.getOriginOfOrder());
            order.setCustomerIdentifier(orderRequest.getCustomerId());
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(partnerPartnerConfirmation.getCurrentStatus());
        } catch (OrderFlightException exception) {
            if (!exception.getOrderFlightErrorType().equals(OrderFlightErrorType.ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR)) {
                log.error("ConfirmationService.confirmOrder - error on order confirmation! id: [{}] ", id, exception);
                throw exception;
            }

            //PMA
            var entries = DynatraceUtils.buildEntries(exception.getOrderFlightErrorType(), exception.getArgs());
            DynatraceUtils.setDynatraceErrorEntries(entries);
            log.warn("ConfirmationService.confirmOrder - Error on order confirmation! Sent to PMA process! id: [{}]", id, exception);
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getMessage()));
        } catch (Exception exception) {
            if (order != null) {
                var entries = DynatraceUtils.buildEntries(ORDER_FLIGHT_INTERNAL_ERROR, "Unknown error on confirm order!");
                DynatraceUtils.setDynatraceErrorEntries(entries);
                //PMA
                log.warn("ConfirmationService.confirmOrder Unknown error on confirm order - id: [{}], orderId: [{}], transactionId: [{}] ", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), exception);
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getLocalizedMessage()));
        }

        if (order != null) {
            orderService.addNewOrderStatus(order, status);
            orderService.save(order);
            orderService.orderDetailLog("confirmOrder", status.getCode(), order);
            log.info("ConfirmationService.confirmOrder - End - id: [{}], orderId: [{}], transactionId: [{}], statusCode: [{}], partnerCode: [{}] ", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), status.getCode(), order.getPartnerCode());
        }
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }

    public void orderProcess(OrderProcess orderProcess) {
        OrderCurrentStatusEntity status = null;

        var order = orderService.getOrderById(orderProcess.getId());
        var oldStatusCode = order.getCurrentStatus().getCode();

        if (!orderService.isSameStatus(StatusLivelo.PROCESSING.getCode(), oldStatusCode)) {
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

        if (!orderService.isSameStatus(oldStatusCode, status.getCode())) {
            Duration duration = processOrderTimeDifference(order.getCurrentStatus().getCreateDate());
            log.info("ConfirmationService.processOrderTimeDifference - process order diff time - minutes: [{}], orderId: [{}], partnerCode: [{}], oldStatus: [{}], newStatus: [{}]", duration.toMinutes(), order.getId(), order.getPartnerCode(), order.getCurrentStatus(), status);
        }

        orderService.incrementProcessCounter(processCounter);
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
        orderService.updateOrderOnLiveloPartners(order, oldStatusCode);

        log.info("ConfirmationService.orderProcess - order process counter - id: [{}], count: [{}]", order.getId(), processCounter.getCount());
        orderService.orderDetailLog("orderProcess", status.getCode(), order);
    }

    private OrderCurrentStatusEntity processGetConfirmation(OrderEntity order) {
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

    private PartnerConfirmOrderStatusResponse buildStatusToFailed(String cause) {
        return PartnerConfirmOrderStatusResponse
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