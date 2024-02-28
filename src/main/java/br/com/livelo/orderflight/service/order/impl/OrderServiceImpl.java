package br.com.livelo.orderflight.service.order.impl;

import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.OrderProcessMapper;
import br.com.livelo.orderflight.repository.ItemRepository;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderProcessMapper orderMapper;

    private final ItemRepository itemRepository;

    @Value("${order.orderProcessMaxRows}")
    private int orderProcessMaxRows;

    public OrderEntity getOrderById(String id) throws OrderFlightException {
        Optional<OrderEntity> order = orderRepository.findById(id);

        if (order.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_ORDER_NOT_FOUND;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        return order.get();
    }
    
    public void addNewOrderStatus(OrderEntity order, OrderStatusEntity status) {
        order.getStatusHistory().add(status);
        order.setCurrentStatus(status);
    }
    public OrderItemEntity getFlightFromOrderItems(Set<OrderItemEntity> orderItemsEntity) throws OrderFlightException {
        Optional<OrderItemEntity> itemFlight = orderItemsEntity.stream().filter(item -> !item.getSkuId().toLowerCase().contains("tax")).findFirst();

        if (itemFlight.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_ORDER_NOT_FOUND;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        return itemFlight.get();
    }

    public void updateVoucher(OrderItemEntity orderItem, String voucher) {
        orderItem.getTravelInfo().setVoucher(voucher);
    }

    public Optional<OrderEntity> findByCommerceOrderId(String commerceOrderId) {
        return this.orderRepository.findByCommerceOrderId(commerceOrderId);
    }

    public void delete(OrderEntity order) {
        this.orderRepository.delete(order);
    }

    public OrderEntity save(OrderEntity order) {
        return this.orderRepository.save(order);
    }

    public PaginationOrderProcessResponse getOrdersByStatusCode(String statusCode, Integer page, Integer rows) throws OrderFlightException {
        if (page <= 0) {
            throw new OrderFlightException(OrderFlightErrorType.VALIDATION_INVALID_PAGINATION, OrderFlightErrorType.VALIDATION_INVALID_PAGINATION.getDescription(), null);
        }

        rows = rows > orderProcessMaxRows ? orderProcessMaxRows : rows;
        page = page - 1;

        Pageable pagination = PageRequest.of(page, rows);
        var foundOrders = orderRepository.findAllByCurrentStatusCode(statusCode.toUpperCase(), pagination);
        return orderMapper.pageRepositoryToPaginationResponse(foundOrders);
    }

    public OrderItemEntity findByCommerceItemIdAndSkuId(String commerceItemId, SkuItemResponse skuItemResponseDTO) {
        final Optional<OrderItemEntity> itemOptional = itemRepository.findByCommerceItemIdAndSkuId(commerceItemId, skuItemResponseDTO.getSkuId());

        if (itemOptional.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_COMMERCE_ITEM_ID_OR_ID_SKU_NOT_FOUND;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        return itemOptional.get();
    }

}
