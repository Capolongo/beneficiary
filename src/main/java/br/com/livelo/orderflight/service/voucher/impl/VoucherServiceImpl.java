package br.com.livelo.orderflight.service.voucher.impl;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.voucher.VoucherService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    @Override
    public void orderProcess(OrderProcess orderProcess) throws JsonProcessingException {
        OrderStatusEntity status = null;
        var order = orderService.getOrderById(orderProcess.getId());

        if (!orderService.isSameStatus(StatusConstants.VOUCHER.getCode(), order.getCurrentStatus().getCode())) {
            log.warn("VoucherServiceImpl.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var processCounter = orderService.getProcessCounter(order, Webhooks.VOUCHER.value);
        if (ChronoUnit.DAYS.between(processCounter.getCreateDate(), ZonedDateTime.now()) > 2) {
            log.warn("VoucherServiceImpl.orderProcess - counter exceeded limit - id: [{}]", order.getId());
            status = orderService.buildOrderStatusFailed("O contador excedeu o limite de tentativas");
        } else {
            status = processVoucher(order);
        }

        orderService.addNewOrderStatus(order, status);
        orderService.incrementProcessCounter(processCounter);
        orderService.save(order);

        log.info("VoucherServiceImpl.orderProcess - order process counter - count: [{}]", processCounter.getCount());
    }
    private OrderStatusEntity processVoucher(OrderEntity order) {
        try {
            var connectorVoucherOrderResponse = connectorPartnersProxy.getVoucherOnPartner(order.getPartnerCode(), order.getId());
            var status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorVoucherOrderResponse.getCurrentStatus());

            if (!connectorVoucherOrderResponse.getVoucher().isEmpty()) {
                status.setCode(StatusConstants.COMPLETED.getCode());
                var itemFlight = orderService.getFlightFromOrderItems(order.getItems());
                orderService.updateVoucher(itemFlight, connectorVoucherOrderResponse.getVoucher());
            }

            return status;
        } catch (OrderFlightException exception) {
            return order.getCurrentStatus();
        }
    }
}
