package br.com.livelo.orderflight.service.completed;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.service.completed.impl.CompletedService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompletedServiceImpl implements CompletedService {
    private final OrderServiceImpl orderService;

    @Override
    public void orderProcess(OrderProcess orderProcess) throws JsonProcessingException {
        log.info("CompletedServiceImpl.orderProcess - Start - id: [{}]", orderProcess.getId());

        OrderStatusEntity status = null;

        var order = orderService.getOrderById(orderProcess.getId());
        var currentStatus = order.getCurrentStatus();

        if (!orderService.isSameStatus(StatusConstants.VOUCHER_SENT.getCode(), currentStatus.getCode())) {
            log.warn("CompletedServiceImpl.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        currentStatus.setCode(StatusConstants.COMPLETED.getCode());

        orderService.addNewOrderStatus(order, currentStatus);
        orderService.save(order);

        log.info("CompletedServiceImpl.orderProcess - order completed - id: [{}]", order.getId());
    }
}
