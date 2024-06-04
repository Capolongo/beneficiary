package br.com.livelo.orderflight.service.order.impl;

import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestItemDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateDetailDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateItemDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.LiveloPartnersMapper;
import br.com.livelo.orderflight.mappers.OrderProcessMapper;
import br.com.livelo.orderflight.proxies.LiveloPartnersProxy;
import br.com.livelo.orderflight.repository.ItemRepository;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.*;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final String VALIDATE_ORDER_RESULT_VALID = "valid";
    private static final String VALIDATE_ORDER_RESULT_INVALID = "invalid";
    private static final int MINUTES_BEFORE_EXPIRE = 15;
    private static final String EXPIRED_ORDER_MESSAGE = "O tempo máximo para o resgate de viagem foi excedido. Pode tentar novamente?";


    public static final String TAX = "tax";
    private final OrderRepository orderRepository;
    private final OrderProcessMapper orderMapper;
    private final ItemRepository itemRepository;
    private final LiveloPartnersProxy liveloPartnersProxy;
    private final LiveloPartnersMapper liveloPartnersMapper;

    @Value("${order.orderProcessMaxRows}")
    private int orderProcessMaxRows;

    public OrderEntity getOrderById(String id) throws OrderFlightException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_ORDER_NOT_FOUND, null, "Order not found with id: " + id));
    }

    public void addNewOrderStatus(OrderEntity order, OrderCurrentStatusEntity status) {
        if (isSameStatus(status.getCode(), order.getCurrentStatus().getCode())) {
            return;
        }

        var statusHistory = orderMapper.statusHistoryToCurrentStatus(status);

        order.getStatusHistory().add(statusHistory);
        order.setCurrentStatus(status);
    }

    public OrderItemEntity getFlightFromOrderItems(Set<OrderItemEntity> orderItemsEntity) throws OrderFlightException {
        return orderItemsEntity.stream()
                .filter(this::isFlightItem)
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, null));
    }

    public boolean isFlightItem(OrderItemEntity item) {
        return !item.getSkuId().toLowerCase().contains(TAX);
    }

    public OrderItemEntity getTaxFromOrderItems(Set<OrderItemEntity> orderItemsEntity) throws OrderFlightException {
        return orderItemsEntity.stream()
                .filter(item -> !isFlightItem(item))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Tax item not found"));
    }

    public void orderDetailLog(String invokedBy, String newStatusCode, OrderEntity order) {
        OrderItemEntity flightItem = getFlightFromOrderItems(order.getItems());
        OrderItemEntity taxItem = getTaxFromOrderItems(order.getItems());
        log.info("OrderService.orderDetailLog - " + invokedBy + " - orderId: [{}], partnerCode: [{}], statusCode: [{}], amount: [{}], pointsAmount: [{}], partnerAmount: [{}], flightAmount: [{}], flightPointsAmount: [{}], flightPartnerAmount: [{}], taxAmount: [{}], taxPointsAmount: [{}], taxPartnerAmount: [{}],", order.getId(), order.getPartnerCode(), newStatusCode, order.getPrice().getAmount(), order.getPrice().getPointsAmount(), order.getPrice().getPartnerAmount(), flightItem.getPrice().getAmount(), flightItem.getPrice().getPointsAmount(), flightItem.getPrice().getPartnerAmount(), taxItem.getPrice().getAmount(), taxItem.getPrice().getPointsAmount(), taxItem.getPrice().getPartnerAmount());
    }

    public boolean isSameStatus(String currentStatus, String newStatus) {
        return currentStatus.equals(newStatus);
    }

    public OrderCurrentStatusEntity buildOrderStatusFailed(String cause) {
        return OrderCurrentStatusEntity.builder()
                .partnerCode(String.valueOf(500))
                .code(StatusLivelo.FAILED.getCode())
                .partnerResponse(cause)
                .partnerDescription("failed")
                .description(StatusLivelo.FAILED.getDescription())
                .build();
    }

    public void incrementProcessCounter(ProcessCounterEntity processCounter) {
        processCounter.setCount(processCounter.getCount() + 1);
    }

    public ProcessCounterEntity getProcessCounter(OrderEntity order, String process) {
        var processCounter = order.getProcessCounters().stream().filter(counter -> process.equals(counter.getProcess())).findFirst();

        if (processCounter.isEmpty()) {
            var newProcessCounter = ProcessCounterEntity.builder().process(process).count(0).build();
            order.getProcessCounters().add(newProcessCounter);
            return newProcessCounter;
        }

        return processCounter.get();
    }

    public void updateVoucher(OrderItemEntity orderItem, String voucher) {
        orderItem.getTravelInfo().setVoucher(voucher);
        if (voucher != null) {
            log.info("OrderService.updateVoucher - voucher updated - orderId: [{}]", orderItem.getId());
        }
    }

    public void updateSubmittedDate(OrderEntity order, String date) {
        LocalDateTime submitDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            submitDate = LocalDateTime.parse(date, formatter);
        } catch (Exception e) {
            log.warn("OrderService.updateSubmittedDate - error on parse submittedDate for order id: [{}]", order.getId());
            submitDate = LocalDateTime.now();
        }

        order.setSubmittedDate(submitDate);
    }

    public Optional<OrderEntity> findByCommerceOrderIdInAndExpirationDateAfter(List<String> commerceOrderId) {
        var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var actualDate = LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo")).format(format);

        return this.orderRepository.findByCommerceOrderIdInAndExpirationDateAfter(commerceOrderId, actualDate);
    }

    public void delete(OrderEntity order) {
        this.orderRepository.delete(order);
    }

    public OrderEntity save(OrderEntity order) {
        return this.orderRepository.save(order);
    }

    public PaginationOrderProcessResponse getOrdersByStatusCode(String statusCode, Optional<String> limitArrivalDate, Integer page, Integer rows)
            throws OrderFlightException {
        Pageable pagination = pageRequestOf(page, rows);

        Page<OrderProcess> foundOrders;
        if (limitArrivalDate.isPresent()) {
            var arglimitArrivalDate = LocalDate.parse(limitArrivalDate.get(), DateTimeFormatter.ISO_DATE).atTime(0, 0);
            foundOrders = orderRepository.findAllByCurrentStatusCodeAndArrivalDateLessThan(statusCode.toUpperCase(),
                    arglimitArrivalDate, pagination);
        } else {
            foundOrders = orderRepository.findAllByCurrentStatusCode(statusCode.toUpperCase(), pagination);
        }

        return orderMapper.pageRepositoryToPaginationResponse(foundOrders);
    }

    public OrderItemEntity findByCommerceItemIdAndSkuId(String commerceItemId, SkuItemResponse skuItemResponseDTO) {
        final Optional<OrderItemEntity> itemOptional = itemRepository.findByCommerceItemIdAndSkuId(commerceItemId, skuItemResponseDTO.getSkuId());
        log.info("findByCommerceItemIdAndSkuId - id: [{}], item: [{}]", commerceItemId, itemOptional);
        if (itemOptional.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_COMMERCE_ITEM_ID_OR_ID_SKU_NOT_FOUND;
            log.warn("findByCommerceItemIdAndSkuId - id: [{}], error: [{}]", commerceItemId, errorType);
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        return itemOptional.get();
    }

    public OrderValidateResponseDTO validateOrderList(OrderValidateRequestDTO orderValidateRequest) throws OrderFlightException {
        Optional<OrderEntity> order = this.findByCommerceOrderIdInAndExpirationDateAfter(List.of(orderValidateRequest.getId()));
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

    public void updateOrderOnLiveloPartners(OrderEntity order, String oldStatus) {
        if (isSameStatus(order.getCurrentStatus().getCode(), oldStatus)) {
            return;
        }

        UpdateOrderDTO updateOrderDTO = liveloPartnersMapper.orderEntityToUpdateOrderDTO(order);
        liveloPartnersProxy.updateOrder(order.getCommerceOrderId(), updateOrderDTO);
    }

    private Pageable pageRequestOf(Integer page, Integer rows) throws OrderFlightException {
        if (page <= 0) {
            throw new OrderFlightException(OrderFlightErrorType.VALIDATION_INVALID_PAGINATION,
                    OrderFlightErrorType.VALIDATION_INVALID_PAGINATION.getDescription(), null);
        }
        return PageRequest.of(page - 1, rows > orderProcessMaxRows ? orderProcessMaxRows : rows);
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
