package br.com.livelo.orderflight.service.order.impl;

import br.com.livelo.orderflight.configs.order.consts.StatusConstants;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ConnectorPartnersProxy connectorPartnersProxy;
    private final ConfirmOrderMapper confirmOrderMapper;


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

//          todo: verificar se quantidade de vezes é >= 45 e setar como falha se for


        var connectorConfirmOrderResponse = connectorPartnersProxy.getConfirmationOnPartner(order.getPartnerCode(), order.getId());
        var status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderResponse.getCurrentStatus());

        if (isSameStatus(currentStatusCode, status.getCode())) {
//                TODO: incrementar a quantidade de chamadas na tabela
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
}
