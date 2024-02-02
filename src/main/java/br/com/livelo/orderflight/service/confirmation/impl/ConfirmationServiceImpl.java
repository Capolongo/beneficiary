package br.com.livelo.orderflight.service.confirmation.impl;

import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.configs.order.consts.Constants;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.ReservationException;
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

  public ConfirmOrderResponse confirmOrder(String id, ConfirmOrderRequest orderRequest) throws Exception {
    OrderEntity order = null;
    OrderStatusEntity status;
    try {
      log.info("ConfirmationService.confirmOrder() - Start");
      order = orderService.getOrderById(id);

      log.info("ConfirmationService.confirmOrder() - id: [{}], orderId: [{}], transactionId: [{}],  order: [{}]",
          order.getId(), order.getCommerceOrderId(), order.getTransactionId(), order);

      ConfirmOrderValidation.validateOrderPayload(orderRequest, order);

      ConnectorConfirmOrderRequest connectorConfirmOrderRequest = confirmOrderMapper
          .orderEntityToConnectorConfirmOrderRequest(order);

      ConnectorConfirmOrderResponse connectorPartnerConfirmation = connectorPartnersProxy
          .confirmOnPartner(
              orderRequest.getPartnerCode(),
              connectorConfirmOrderRequest);
      order.setPartnerOrderId(connectorPartnerConfirmation.getPartnerOrderId());
      status = confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(
          connectorPartnerConfirmation.getCurrentStatus());
    } catch (ReservationException exception) {
      throw exception;
    } catch (Exception exception) {
      status = confirmOrderMapper.ConnectorConfirmOrderStatusResponseToStatusEntity(
          buildStatusToFailed(exception.getMessage()));
    }
    orderService.addNewOrderStatus(order, status);
    orderService.save(order);
    log.info("ConfirmationService.confirmOrder() - End");
    return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
  }

  private ConnectorConfirmOrderStatusResponse buildStatusToFailed(String cause) {
    return ConnectorConfirmOrderStatusResponse
        .builder()
        .partnerCode(String.valueOf(500))
        .code(Constants.FAILED)
        .partnerResponse(cause)
        .partnerDescription("failed")
        .description("failed")
        .statusDate(LocalDateTime.now())
        .build();
  }
}
