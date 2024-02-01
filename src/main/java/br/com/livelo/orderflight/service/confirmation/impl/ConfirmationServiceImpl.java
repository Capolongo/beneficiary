package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderService orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws Exception {
        OrderEntity order = null;
        ConnectorConfirmOrderRequest connectorConfirmOrderRequest = null;
        ConnectorConfirmOrderResponse connectorPartnerConfirmation = null;
        try {
            log.info("ConfirmationService.confirmOrder() - Start");
            order = orderService.getOrderById(id);
            log.info("ConfirmationService.confirmOrder() - order: [{}]", order);

            ConfirmOrderValidation.validateOrderPayload(orderRequest, order);

            connectorConfirmOrderRequest = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(order);

            connectorPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(
                    orderRequest.getPartnerCode(),
                    connectorConfirmOrderRequest
            );
            order.setPartnerOrderId(connectorPartnerConfirmation.getPartnerOrderId());
        } catch (Exception exception) {
            connectorConfirmOrderRequest.;
        }


        order = orderService.addNewOrderStatus(order, connectorPartnerConfirmation.getCurrentStatus());
        orderService.save(order);
        log.info("ConfirmationService.confirmOrder() - End");
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }


    private void buildStatusToFailed() {
        ConnectorConfirmOrderStatusResponse failedStatus = ConnectorConfirmOrderStatusResponse
                .builder()
                .partnerCode(String.valueOf(500))
//                    .partnerCode(String.valueOf(exception.status()))
                .code("LIVPNR-1014")
                .partnerResponse(exception.getMessage())
                .partnerDescription("failed")
                .description("failed")
                .statusDate(LocalDateTime.now())
                .build();

        ConnectorConfirmOrderResponse
                .builder()
                .partnerOrderId(connectorConfirmOrderRequest.getPartnerOrderId())
                .partnerCode(connectorConfirmOrderRequest.getPartnerCode())
                .submittedDate(connectorConfirmOrderRequest.getSubmittedDate())
                .expirationDate(connectorConfirmOrderRequest.getExpirationDate())
                .currentStatus(failedStatus)
                .voucher(null)
                .build();
    }




//    private StatusEntity updateStatusFailed(StatusEntity statusEntity, String message, String id, String commerceOrderId){
//        log.info("ProcessOrderServiceImpl.updateStatusFailed - failed transfer, updating order status to failed, id: {}, commerceOrderId: {} ",id, commerceOrderId);
//        final StatusConstant statusFailed = StatusConstant.FAILED;
//        final String finalMessage = createMessageStatusFailed(message, statusFailed);
//
//        return statusEntity.toBuilder()
//                .code(statusFailed.getCode())
//                .description(statusFailed.getDescription())
//                .details(finalMessage)
//                .build();
//
//    }
}
