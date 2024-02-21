package br.com.livelo.orderflight.service.order.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.domain.entity.ProcessCounterEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.mappers.OrderProcessMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ConnectorPartnersProxy connectorPartnersProxy;
    private final ConfirmOrderMapper confirmOrderMapper;

    private final OrderProcessMapper orderMapper;

    @Value("${order.orderProcessMaxRows}")
    private int orderProcessMaxRows;

//    private int maxProcessCountFailed = 48;
    private int maxProcessCountFailed = 3;

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

    public void orderProcess(OrderProcess orderProcess) {
//      1 - consumir mensagem que vai ter o ID do pedido DONE
//      2 - Com o id, buscar na base DONE
//      3 - se nao encontrar o processo é finalizado (obs: analizar se realmente está certo)
//      4 - com os dados do pedido, usaremos o partnercode do pedido para buscar o webhook usando a lib DONE
//      5 - bater no webhook e salvar o status history e currentStatus q for retornado DONE
//      6 - incrementar contador que conta quantas vezes o pedido passou no processo e adicionar o status retornado


        var order = getOrderById(orderProcess.getId());
        var currentStatusCode = order.getCurrentStatus().getCode();


        if (!isSameStatus(currentStatusCode, StatusConstants.PROCESSING.getCode())) {
            return;
        }


        var processCounter = findProcessCounterByWebhook(order.getProcessCounters(), Webhooks.GETCONFIRMATION);
        if (processCounter.getCount() >= maxProcessCountFailed) {
            addNewOrderStatus(order, buildOrderStatusFailed());
            save(order);
            return;
        }
//          todo: verificar se quantidade de vezes é >= 48 e setar como falha se for

        var connectorConfirmOrderResponse = connectorPartnersProxy.getConfirmationOnPartner(order.getPartnerCode(), order.getId());
        var status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderResponse.getCurrentStatus());

        if (isSameStatus(currentStatusCode, status.getCode())) {
            incrementProcessCounter(processCounter);
            save(order);
            return;
        }

        var itemFlight = getFlightFromOrderItems(order.getItems());
        updateVoucher(itemFlight, connectorConfirmOrderResponse.getVoucher());
        addNewOrderStatus(order, status);
        orderRepository.save(order);
    }

    public boolean isSameStatus(String currentStatus, String newStatus) {
        return currentStatus.equals(newStatus);
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

    private void incrementProcessCounter(ProcessCounterEntity processCounter) {
        processCounter.setCount(processCounter.getCount() + 1);
    }

    private ProcessCounterEntity findProcessCounterByWebhook(Set<ProcessCounterEntity> processCounterEntities, Webhooks webhook) {
        var processCounter = processCounterEntities.stream().filter(counter -> webhook.value.equals(counter.getProcess())).findFirst();


        if (processCounter.isEmpty()) {
            var builder = ProcessCounterEntity.builder().process(Webhooks.GETCONFIRMATION.value).count(1).build();
            processCounterEntities.add(builder);
            return builder;
        }

        return processCounter.get();
    }

    private OrderStatusEntity buildOrderStatusFailed() {
        return OrderStatusEntity.builder()
                .partnerCode(String.valueOf(500))
                .code(StatusConstants.FAILED.getCode())
                .partnerResponse("")
                .partnerDescription("failed")
                .description(StatusConstants.FAILED.getDescription())
                .statusDate(LocalDateTime.now())
                .build();
    }
}
