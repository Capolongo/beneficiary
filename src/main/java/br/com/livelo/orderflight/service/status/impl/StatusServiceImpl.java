package br.com.livelo.orderflight.service.status.impl;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusItemDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.StatusMapper;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.status.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        validCommerceOrderIdEqualOrderId(request, order);

        validItemsCommerceItemIdEqualCommerceItemId(request, order);

        validStatusInitial(request, order);

        validStatusCanceledOrCompleted(order);

        final UpdateStatusDTO statusDTO = getStatusFromItem(request);

        setOrderNewStatus(order, statusDTO);

        orderService.save(order);

        return confirmOrderMapper.orderEntityToConfirmOrderResponse(order);
    }

    private static void validStatusCanceledOrCompleted(OrderEntity order) {
        if(order.getCurrentStatus().getCode().equals(StatusLivelo.CANCELED.getCode()) || order.getCurrentStatus().getCode().equals(StatusLivelo.COMPLETED.getCode())) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_CANCELED_OR_COMPLETED;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }

    private static void validStatusInitial(UpdateStatusRequest request, OrderEntity order) {
        if(order.getCurrentStatus().getCode().equals(StatusLivelo.INITIAL.getCode()) && request.getItems().stream().anyMatch(item -> item.getStatus().getMessage().toUpperCase().contains("PROCESS"))) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_INITIAL_CANNOT_PROCESSING;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }

    private static void validItemsCommerceItemIdEqualCommerceItemId(UpdateStatusRequest request, OrderEntity order) {
        order.getItems().forEach(itemStatus -> {
            Boolean itemsIsEmpty = validCommerceItemId(request, itemStatus.getCommerceItemId());
            if(itemsIsEmpty) {
                OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_ITEMS_COMMERCE_ITEM_ID_NOT_EQUAL_COMMERCE_ITEM_ID;
                throw new OrderFlightException(errorType, errorType.getTitle(), null);
            }
        });
    }

    private static void validCommerceOrderIdEqualOrderId(UpdateStatusRequest request, OrderEntity order) {
        if(!order.getCommerceOrderId().equals(request.getOrderId())) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_COMMERCE_ORDER_ID_NOT_EQUAL_ORDER_ID;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }

    private UpdateStatusDTO getStatusFromItem(UpdateStatusRequest updateStatusRequestDTO){
        return updateStatusRequestDTO.getItems()
                .stream()
                .map(UpdateStatusItemDTO::getStatus)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderEntity setOrderNewStatus(OrderEntity orderEntity, UpdateStatusDTO newStatus){
        final OrderStatusEntity statusEntity = statusMapper.convert(newStatus);
        statusEntity.setId(orderEntity.getCurrentStatus().getId());
        orderEntity.setCurrentStatus(statusEntity);
        return orderEntity;
    }

    private static Boolean validCommerceItemId(UpdateStatusRequest request, String commerceItemId) {
        return request.getItems().stream().filter(item ->
                item.getCommerceItemId().equals(commerceItemId)
        ).findFirst().isEmpty();
    }
}
