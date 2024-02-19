package br.com.livelo.orderflight.service.reservation.impl;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.PricingCalculateRequestMapper;
import br.com.livelo.orderflight.mappers.ReservationMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.proxies.PricingProxy;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final OrderService orderService;
    private final ConnectorPartnersProxy partnerConnectorProxy;
    private final PricingProxy pricingProxy;
    private final ReservationMapper reservationMapper;

    public ReservationResponse createOrder(ReservationRequest request, String transactionId, String customerId,
                                           String channel, String listPrice) {
        try {
            var orderOptional = this.orderService.findByCommerceOrderId(request.getCommerceOrderId());

            if (this.isSameOrderItems(request, orderOptional)) {
                orderOptional.ifPresent(this.orderService::delete);
            }

            var partnerReservationResponse = partnerConnectorProxy
                    .createReserve(reservationMapper.toPartnerReservationRequest(request), transactionId);

            var pricingCalculateResponse = pricingProxy.calculate(PricingCalculateRequestMapper.toPricingCalculateRequest(partnerReservationResponse));
            var pricingCalculatePrice = pricingCalculateResponse[0].getPrices().stream().filter(price -> listPrice.equals(price.getPriceListId())).findFirst().orElse(null);
            var orderEntity = reservationMapper.toOrderEntity(request, partnerReservationResponse, transactionId,
                    customerId, channel, listPrice, pricingCalculatePrice);

            this.orderService.save(orderEntity);
            log.info("Order created Order: {} transactionId: {} listPrice: {}", orderEntity.toString(), transactionId,
                    listPrice);
            // deve vir do connector
            return reservationMapper.toReservationResponse(orderEntity, 15);
        } catch (OrderFlightException e) {
            throw e;
        } catch (Exception e) {
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }


    public boolean isSameOrderItems(ReservationRequest request, Optional<OrderEntity> orderOptional) {
        return orderOptional.map(order -> {
            if (order.getItems().size() == request.getItems().size()) {
                var orderCommerceItemsIds = this.getOrderCommerceItemsIds(order);
                var requestItemsIds = this.getRequestItemsIds(request);

                var orderTokens = this.getOrderTokens(order);
                var requestTokens = new HashSet<>(request.getSegmentsPartnerIds());
                var isSameCommerceItemsId = requestItemsIds.containsAll(orderCommerceItemsIds);

                if (isSameCommerceItemsId) {
                    this.hasSameTokens(orderTokens, requestTokens);
                }
                return isSameCommerceItemsId;
            }
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR,
                    "Quantidades de itens diferentes", null);
        }).orElse(false);
    }

    private Set<String> getOrderCommerceItemsIds(OrderEntity order) {
        return order.getItems().stream()
                .map(OrderItemEntity::getCommerceItemId)
                .collect(Collectors.toSet());
    }

    private Set<String> getOrderTokens(OrderEntity order) {
        return order.getItems().stream()
                .flatMap(orderItem -> orderItem.getSegments().stream()
                        .map(SegmentEntity::getPartnerId))
                .collect(Collectors.toSet());
    }

    private Set<String> getRequestItemsIds(ReservationRequest request) {
        return request.getItems().stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toSet());
    }

    private void hasSameTokens(Set<String> orderTokens, Set<String> requestTokens) {
        if (orderTokens.size() != requestTokens.size() || !orderTokens.containsAll(requestTokens)) {
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR,
                    "Tokens do parceiro divergentes", null);
        }
    }
}
