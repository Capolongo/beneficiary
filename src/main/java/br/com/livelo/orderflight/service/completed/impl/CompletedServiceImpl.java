package br.com.livelo.orderflight.service.completed.impl;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.service.completed.CompletedService;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        if (!orderService.isSameStatus(StatusLivelo.VOUCHER_SENT.getCode(), currentStatus.getCode())) {
            log.warn("CompletedServiceImpl.orderProcess - order has different status - id: [{}]", order.getId());
            return;
        }

        var newStatus = OrderStatusEntity.builder()
                .code(StatusLivelo.COMPLETED.getCode())
                .description(StatusLivelo.COMPLETED.getDescription())
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
