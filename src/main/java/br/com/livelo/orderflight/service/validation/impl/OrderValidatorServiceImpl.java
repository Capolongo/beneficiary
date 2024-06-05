package br.com.livelo.orderflight.service.validation.impl;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateDetailDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateItemDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.validation.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class OrderValidatorServiceImpl implements OrderValidator {
    private final OrderService orderService;
    private static final String VALIDATE_ORDER_RESULT_VALID = "valid";
    private static final String EXPIRED_ORDER_MESSAGE = "O tempo máximo para o resgate de viagem foi excedido. Pode tentar novamente?";

    public OrderValidateResponseDTO validateOrder(OrderValidateRequestDTO orderValidateRequest) throws OrderFlightException {
        Optional<OrderEntity> order = orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of(orderValidateRequest.getId()));
        if (order.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.ORDER_FLIGHT_ORDER_VALIDATION_ERROR;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        List<OrderValidateItemDTO> items = order.get().getItems().stream()
                .map(item -> mapToItemDTO(item, order.get().getId()))
                .collect(toList());

        return OrderValidateResponseDTO
                .builder()
                .status(VALIDATE_ORDER_RESULT_VALID)
                .id(orderValidateRequest.id)
                .items(getDetails(items))
                .build();
    }

    private OrderValidateItemDTO mapToItemDTO(OrderItemEntity orderItem, String partnerOrderId) {
        return OrderValidateItemDTO.builder()
                .id(orderItem.getSkuId())
                .partnerOrderId(partnerOrderId)
                .commerceItemId(orderItem.getCommerceItemId())
                .valid(Boolean.TRUE)
                .build();
    }

    private List<OrderValidateItemDTO> getDetails(List<OrderValidateItemDTO> items) {
        items.forEach(item -> item.setDetails(
                Collections.singletonList(OrderValidateDetailDTO.builder().message(item.getValid() ? "" : EXPIRED_ORDER_MESSAGE).build())
        ));
        return items;

    }
}
