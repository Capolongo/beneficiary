package br.com.livelo.orderflight.service.order.impl;

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
    private final ObjectMapper objectMapper;
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

    public void orderProcess(OrderProcess orderProcess) throws JsonProcessingException {
//      1 - consumir mensagem que vai ter o ID do pedido DONE
//      2 - Com o id, buscar na base DONE
//      3 - se nao encontrar o processo é finalizado DONE
//      4 - com os dados do pedido, usaremos o partnercode do pedido para buscar o webhook usando a lib
//      5 - bater no webhook e salvar o status history e currentStatus q for retornado
//      6 - incrementar contador que conta quantas vezes o pedido passou no processo e adicionar o status retornado


//        OrderProcess orderProcess = objectMapper.readValue(MessageUtils.extractBodyAsString(payload), OrderProcess.class);
        System.out.println("order id = " + orderProcess.getId());
        var order = getOrderById(orderProcess.getId());

        var connectorConfirmOrderResponse = connectorPartnersProxy.getConfirmationOnPartner(order.getPartnerCode(), order.getId());
        var status = confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(connectorConfirmOrderResponse.getCurrentStatus());

        addNewOrderStatus(order, status);
        System.out.println("order = " + order);
    }
}
