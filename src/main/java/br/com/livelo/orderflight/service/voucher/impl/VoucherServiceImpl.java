package br.com.livelo.orderflight.service.voucher.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.voucher.VoucherService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final OrderServiceImpl orderService;
    private final ConfirmOrderMapper confirmOrderMapper;
    private final ConnectorPartnersProxy connectorPartnersProxy;

    @Value("${order.getVoucherMaxProcessCountFailed}")
    private int errorCount;

    @Override
    public void orderProcess(OrderProcess orderProcess) {
        log.info("VoucherServiceImpl.orderProcess - Confirming the voucher creation - order: [{}]", orderProcess.getId());

        OrderStatusEntity status = null;
        var order = orderService.getOrderById(orderProcess.getId());

        if (!orderService.isSameStatus(StatusConstants.WAIT_VOUCHER.getCode(), order.getCurrentStatus().getCode())) {
            log.warn("VoucherServiceImpl.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var processCounter = orderService.getProcessCounter(order, Webhooks.VOUCHER.value);

        if (processCounter.getCount() >=  errorCount) {
            log.warn("VoucherServiceImpl.orderProcess - counter exceeded limit - id: [{}]", order.getId());
            status = orderService.buildOrderStatusFailed("O contador excedeu o limite de tentativas");
        } else {
            status = processVoucher(order);
        }

        orderService.addNewOrderStatus(order, status);
        orderService.incrementProcessCounter(processCounter);
        orderService.save(order);

        log.info("VoucherServiceImpl.orderProcess - status updated - order: [{}]", order.getId());
    }
    private OrderStatusEntity processVoucher(OrderEntity order) {
        try {
            log.info("VoucherServiceImpl.processVoucher - Checking voucher at partner - order: [{}] partner: [{}]", order.getId(), order.getPartnerCode());
            var connectorVoucherOrderResponse = connectorPartnersProxy.getVoucherOnPartner(order.getPartnerCode(), order.getPartnerOrderId(), order.getId());
            var status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorVoucherOrderResponse.getCurrentStatus());
            var itemFlight = orderService.getFlightFromOrderItems(order.getItems());
            orderService.updateVoucher(itemFlight, connectorVoucherOrderResponse.getVoucher());

            return status;
        } catch (OrderFlightException exception) {
            return order.getCurrentStatus();
        } catch (Exception exception) {
            log.error("VoucherServiceImpl.processVoucher - error - orderId: [{}], exception: [{}]", order.getId(), exception.getCause(), exception);
            return orderService.buildOrderStatusFailed(exception.getMessage());
        }
    }
}