package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.orderflight.service.confirmation.ConfirmationService;
import br.com.livelo.orderflight.utils.ConfirmOrderValidation;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {
    private final OrderService orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws Exception {
        log.info("ConfirmationService.confirmOrder() - Start");
        OrderEntity order = orderService.getOrderById(id);
        log.info("ConfirmationService.confirmOrder() - order: [{}]", order);

        ConfirmOrderValidation.validateIfAlreadyIsConfirmed(order.getCurrentStatus(), orderRequest.getResubmission());
        ConfirmOrderValidation.validateRequestPayload(orderRequest, order);

        ConnectorConfirmOrderRequest connectorConfirmOrderRequest = confirmOrderMapper.orderEntityToConnectorConfirmOrderRequest(order);

        ConnectorConfirmOrderResponse connectorPartnerConfirmation = connectorPartnersProxy.confirmOnPartner(
                orderRequest.getPartnerCode(),
                connectorConfirmOrderRequest
        );
        log.info("ConfirmationService.confirmOrder() - proxy response: [{}]", connectorPartnerConfirmation);

        ConfirmOrderValidation.validatePartnerOrderIds(order.getPartnerOrderId(), connectorPartnerConfirmation.getPartnerOrderId());

//        todo: atualizar o partnerOrderId com o retorno do connector

        order = orderService.addNewOrderStatus(order, connectorPartnerConfirmation.getCurrentStatus());

        log.info("ConfirmationService.confirmOrder() - End");
        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }
}
