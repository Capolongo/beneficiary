package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.ReservationItem;
import br.com.livelo.orderflight.domain.dto.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.orderflight.mappers.ReservationMapper;
import br.com.livelo.orderflight.proxy.PartnerConnectorProxy;
import br.com.livelo.orderflight.service.OrderService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class ReservationService {
    private final OrderService orderService;

    private final PartnerConnectorProxy partnerConnectorProxy;
    private final ReservationMapper reservationMapper;


    public ReservationResponse createOrder(ReservationRequest request, String transactionId, String customerId, String channel, String listPrice) {
        try {
            var orderOptional = this.orderService.findByCommerceOrderId(request.getCommerceOrderId());

            if (this.isSameOrderItems(request, orderOptional)) {
                orderOptional.ifPresent(this.orderService::delete);
            }

            var partnerReservationResponse = partnerConnectorProxy.reservation(reservationMapper.toPartnerReservationRequest(request), transactionId);
            OrderEntity orderEntity = reservationMapper.toOrderEntity(request, partnerReservationResponse, transactionId, customerId, channel, listPrice);

            this.orderService.save(orderEntity);

            return reservationMapper.toCartResponse(orderEntity);
        } catch (ReservationException e) {
            throw e;
        } catch (PersistenceException e) {
            throw new ReservationException(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        } catch (Exception e) {
            //TODO TRATAR EXCEÇÕES DE MAPPER
            throw e;
        }
    }

    private boolean isSameOrderItems(ReservationRequest request, Optional<OrderEntity> orderOptional) {
        return orderOptional.map(order -> {
            if (order.getItems().size() == request.getItems().size()) {
                var orderItemsIds = this.getOrderItemsIds(order);
                var requestItemsIds = this.getRequestItemsIds(request);

                var orderTokens = this.getOrderTokens(order);
                var requestTokens = new HashSet<>(request.getSegmentsPartnerIds());
                this.hasSameTokens(orderTokens, requestTokens);
                return requestItemsIds.containsAll(orderItemsIds);
            }
            throw new ReservationException(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR, "Quantidades de itens diferentes", null);
        }).orElse(false);
    }

    private Set<String> getOrderItemsIds(OrderEntity order) {
        return order.getItems().stream()
                .map(OrderItemEntity::getCommerceItemId)
                .collect(Collectors.toSet());
    }

    private Set<String> getOrderTokens(OrderEntity order) {
        return order.getItems().stream()
                .flatMap(orderItem ->
                        orderItem.getSegments().stream()
                                .map(SegmentEntity::getPartnerId)
                ).collect(Collectors.toSet());
    }

    private Set<String> getRequestItemsIds(ReservationRequest request) {
        return request.getItems().stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toSet());
    }

    private void hasSameTokens(Set<String> orderTokens, Set<String> requestTokens) {
        if (orderTokens.size() != requestTokens.size() || !orderTokens.containsAll(requestTokens)) {
            throw new ReservationException(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, "Tokens do parceiro divergentes", null);
        }
    }
}
