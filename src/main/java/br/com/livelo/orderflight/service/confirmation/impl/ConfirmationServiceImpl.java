package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
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
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    @Value("${order.maxProcessCountFailed}")
    private int maxProcessCountFailed;

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
            if (order != null) {
                log.error("ConfirmationService.confirmOrder exception - id: [{}], orderId: [{}], transactionId: [{}],  error: [{}]", id, orderRequest.getCommerceOrderId(), order.getTransactionId(), exception);
            }
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(buildStatusToFailed(exception.getLocalizedMessage()));
        }
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
        if (order != null) {
            log.info("ConfirmationService.confirmOrder - End - id: [{}], orderId: [{}], transactionId: [{}]", id, orderRequest.getCommerceOrderId(), order.getTransactionId());
        }
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }

    public void orderProcess(OrderProcess orderProcess) {
//      1 - consumir mensagem que vai ter o ID do pedido DONE
//      2 - Com o id, buscar na base DONE
//      3 - se nao encontrar o processo é finalizado (obs: analizar se realmente está certo)
//      4 - com os dados do pedido, usaremos o partnercode do pedido para buscar o webhook usando a lib DONE
//      5 - bater no webhook e salvar o status history e currentStatus q for retornado DONE
//      6 - incrementar contador que conta quantas vezes o pedido passou no processo e adicionar o status retornado

        OrderStatusEntity status = null;
        var order = orderService.getOrderById(orderProcess.getId());
        var currentStatusCode = order.getCurrentStatus().getCode();

        if (!orderService.isSameStatus(StatusConstants.PROCESSING.getCode(), currentStatusCode)) {
            log.warn("ConfirmationService.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var processCounter = orderService.getProcessCounter(order, Webhooks.GETCONFIRMATION.value);
        if (processCounter.getCount() >= maxProcessCountFailed) {
            status = orderService.buildOrderStatusFailed();
        } else {
            var connectorConfirmOrderResponse = connectorPartnersProxy.getConfirmationOnPartner(order.getPartnerCode(), order.getId());
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderResponse.getCurrentStatus());
            var itemFlight = orderService.getFlightFromOrderItems(order.getItems());
            orderService.updateVoucher(itemFlight, connectorConfirmOrderResponse.getVoucher());

        }
        orderService.incrementProcessCounter(processCounter);
        orderService.addNewOrderStatus(order, status);
        orderService.save(order);
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

    private void updateStatusAndSaveOrder(OrderEntity order, OrderStatusEntity status) {
        orderService.addNewOrderStatus(order, status);
    }
}
//
//            if (orderService.isSameStatus(status.getCode(), currentStatusCode)) {
//                    orderService.incrementProcessCounter(processCounter);
//                    orderService.save(order);
//                    return;
//                    }
