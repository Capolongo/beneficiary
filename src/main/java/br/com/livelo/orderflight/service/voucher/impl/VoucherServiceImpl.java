package br.com.livelo.orderflight.service.voucher.impl;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
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

        var processCounter = orderService.getProcessCounter(order, Webhooks.VOUCHER.value);
        if (ChronoUnit.DAYS.between(processCounter.getCreateDate(), ZonedDateTime.now()) > 2) {
            log.warn("VoucherServiceImpl.orderProcess - counter exceeded limit - id: [{}]", order.getId());
            orderService.buildOrderStatusFailed("O contador excedeu o limite de tentativas");
        } else {
            var connectorVoucherOrderResponse = connectorPartnersProxy.getVoucherOnPartner(order.getPartnerCode(), order.getId());
            status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorVoucherOrderResponse.getCurrentStatus());

            if (!connectorVoucherOrderResponse.getVoucher().isEmpty()) {
                status.setCode(StatusConstants.COMPLETED.getCode());
                orderService.addNewOrderStatus(order, status);
                var itemFlight = orderService.getFlightFromOrderItems(order.getItems());
                orderService.updateVoucher(itemFlight, connectorVoucherOrderResponse.getVoucher());
            }
        }
        orderService.incrementProcessCounter(processCounter);
        orderService.save(order);
    }

}
