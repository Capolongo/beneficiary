package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.ConfirmationExceptions.ValidationRequestException;
import br.com.livelo.orderflight.exception.OrderExceptions.OrderNotFoundException;
import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import feign.FeignException;
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
        ConnectorConfirmOrderRequest connectorConfirmOrderRequest;
        ConnectorConfirmOrderResponse connectorPartnerConfirmation;
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
            order = orderService.addNewOrderStatus(order, connectorPartnerConfirmation.getCurrentStatus());
        } catch (OrderNotFoundException | ValidationRequestException exception) {
            throw exception;
        } catch (Exception exception) {
            order = orderService.addNewOrderStatus(order, buildStatusToFailed(exception.getMessage()));
        }
        orderService.save(order);
        log.info("ConfirmationService.confirmOrder() - End");
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }


    private ConnectorConfirmOrderStatusResponse buildStatusToFailed(String cause) {
        return ConnectorConfirmOrderStatusResponse
                .builder()
                .partnerCode(String.valueOf(500))
                .code("LIVPNR-1014")
                .partnerResponse(cause)
                .partnerDescription("failed")
                .description("failed")
                .statusDate(LocalDateTime.now())
                .build();
    }
}
