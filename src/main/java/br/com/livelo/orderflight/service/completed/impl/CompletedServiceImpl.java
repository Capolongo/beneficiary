package br.com.livelo.orderflight.service.completed.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.service.completed.CompletedService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompletedServiceImpl implements CompletedService {
    private final OrderServiceImpl orderService;

    @Override
    public void orderProcess(OrderProcess orderProcess) {
        log.info("CompletedServiceImpl.orderProcess - Start - id: [{}]", orderProcess.getId());

        var order = orderService.getOrderById(orderProcess.getId());
        var currentStatus = order.getCurrentStatus();

        if (!orderService.isSameStatus(StatusConstants.VOUCHER_SENT.getCode(), currentStatus.getCode())) {
            log.warn("CompletedServiceImpl.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var newStatus = OrderStatusEntity.builder()
                .code(StatusConstants.COMPLETED.getCode())
                .description(StatusConstants.COMPLETED.getDescription())
                .partnerCode(currentStatus.getPartnerCode())
                .statusDate(LocalDateTime.now())
                .partnerResponse(currentStatus.getPartnerResponse())
                .partnerDescription(currentStatus.getPartnerDescription())
                .build();

        orderService.addNewOrderStatus(order, newStatus);
        orderService.save(order);

        log.info("CompletedServiceImpl.orderProcess - order completed - id: [{}]", order.getId());
    }
}
