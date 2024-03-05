package br.com.livelo.orderflight.service.status.impl;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusItemDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.StatusMapper;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.status.StatusService;
import br.com.livelo.orderflight.utils.UpdateStatusValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final OrderService orderService;

    private final StatusMapper statusMapper;

    private final ConfirmOrderMapper confirmOrderMapper;

    @Override
    public ConfirmOrderResponse updateStatus(String id, UpdateStatusRequest request) {

        log.info("StatusServiceImpl.updateStatus - start id: [{}], request: [{}]", id, request);

        final OrderEntity order = orderService.getOrderById(id);

        UpdateStatusValidate.validationUpdateStatus(request, order);

        final UpdateStatusDTO statusDTO = getStatusFromItem(request);

        Duration duration = changeStatusTimeDifference(order.getCurrentStatus().getStatusDate());

        updateOrderStatus(order, statusDTO);

        orderService.save(order);

        log.info("StatusServiceImpl.updateStatus - changeStatusTimeDifference minutes: [{}]", duration.toMinutes());

        log.info("StatusServiceImpl.updateStatus() - end - id: [{}], code: [{}], partnerCode[{}]", order.getId(), order.getCurrentStatus().getCode(), order.getPartnerCode() );

        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }


    private UpdateStatusDTO getStatusFromItem(UpdateStatusRequest updateStatusRequestDTO){
        return updateStatusRequestDTO.getItems()
                .stream()
                .map(UpdateStatusItemDTO::getStatus)
                .findFirst()
                .orElse(UpdateStatusDTO.builder().build());
    }

    private void updateOrderStatus(OrderEntity order, UpdateStatusDTO updateStatusDTO){
        final OrderStatusEntity statusEntity = statusMapper.convert(updateStatusDTO);
        orderService.addNewOrderStatus(order, statusEntity);
    }

    private Duration changeStatusTimeDifference(LocalDateTime baseTime) {
        return Duration.between(baseTime, LocalDateTime.now());
    }
}
