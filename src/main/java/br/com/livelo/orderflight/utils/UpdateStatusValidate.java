package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateStatusValidate {

    private static final String process = "PROCESS";

    public static void validadionUpdateStatus(UpdateStatusRequest request, OrderEntity order) {
        validCommerceOrderIdEqualOrderId(request, order);
        validItemsCommerceItemIdEqualCommerceItemId(request, order);
        validStatusInitial(request, order);
        validStatusCanceledOrCompleted(order);

    }

    private static void validCommerceOrderIdEqualOrderId(UpdateStatusRequest request, OrderEntity order) {
        if(!order.getCommerceOrderId().equals(request.getOrderId())) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_COMMERCE_ORDER_ID_NOT_EQUAL_ORDER_ID;
            log.error("UpdateStatusValidate.validCommerceOrderIdEqualOrderId - commerceOrderId: [{}], orderId: [{}]", order.getCommerceOrderId(), request.getOrderId());
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }

    private static void validItemsCommerceItemIdEqualCommerceItemId(UpdateStatusRequest request, OrderEntity order) {
        order.getItems().forEach(itemStatus -> {
            Boolean itemsIsEmpty = validCommerceItemId(request, itemStatus.getCommerceItemId());
            if(itemsIsEmpty) {
                OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_ITEMS_COMMERCE_ITEM_ID_NOT_EQUAL_COMMERCE_ITEM_ID;
                log.error("UpdateStatusValidate.validItemsCommerceItemIdEqualCommerceItemId - items [{}], commerceItemId: [{}]", request.getItems(), itemStatus.getCommerceItemId());
                throw new OrderFlightException(errorType, errorType.getTitle(), null);
            }
        });
    }

    private static Boolean validCommerceItemId(UpdateStatusRequest request, String commerceItemId) {
        return request.getItems().stream().filter(item ->
                item.getCommerceItemId().equals(commerceItemId)
        ).findFirst().isEmpty();
    }

    private static void validStatusInitial(UpdateStatusRequest request, OrderEntity order) {
        if(order.getCurrentStatus().getCode().equals(StatusLivelo.INITIAL.getCode()) && validIsProcess(request)) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_INITIAL_CANNOT_PROCESSING;
            log.error("UpdateStatusValidate.validStatusInitial - code [{}], items: [{}]", order.getCurrentStatus().getCode(), request.getItems());
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }

    private static boolean validIsProcess(UpdateStatusRequest request) {
        return request.getItems().stream().anyMatch(item -> item.getStatus().getMessage().toUpperCase().contains(process));
    }

    private static void validStatusCanceledOrCompleted(OrderEntity order) {
        if(order.getCurrentStatus().getCode().equals(StatusLivelo.CANCELED.getCode()) || order.getCurrentStatus().getCode().equals(StatusLivelo.COMPLETED.getCode())) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_STATUS_CANCELED_OR_COMPLETED;
            log.error("UpdateStatusValidate.validStatusCanceledOrCompleted - code [{}]", order.getCurrentStatus().getCode());
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }
    }
}
