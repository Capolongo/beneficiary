package br.com.livelo.orderflight.service.reservation.impl;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.PriceModalityEntity;
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
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.livelo.orderflight.constants.DynatraceConstants.STATUS;
import static br.com.livelo.orderflight.enuns.StatusLivelo.PROCESSING;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final OrderService orderService;
    private final ConnectorPartnersProxy partnerConnectorProxy;
    private final PricingProxy pricingProxy;
    private final ReservationMapper reservationMapper;
    private static final String TAX = "tax";

    public ReservationResponse createOrder(ReservationRequest request, String transactionId, String customerId, String channel, String listPriceId) {
        log.info("ReservationServiceImpl.createOrder - Creating Order: {} transactionId: {} listPriceId: {}", request, transactionId, listPriceId);
        OrderEntity order = null;
        try {
            PartnerReservationResponse partnerReservationResponse = null;
            var orderOptional = this.orderService.findByCommerceOrderId(request.getCommerceOrderId());
            if (orderOptional.isPresent()) {
                order = orderOptional.get();
                if (this.isSameOrderItems(request, orderOptional)) {
                    partnerReservationResponse = this.getPartnerOrder(orderOptional.get().getPartnerOrderId(), transactionId, request.getPartnerCode(), request.getSegmentsPartnerIds());
                    log.info("Order reserved on partner! Proceed with pricing. {}! order: {} transactionId: {}", request.getPartnerCode(), request.getCommerceOrderId(), transactionId);
                } else {
                    this.orderService.delete(order);
                }
            }

            if (this.hasPartnerReserveExpired(partnerReservationResponse)) {
                var partnerReservationRequest = reservationMapper.toPartnerReservationRequest(request);
                partnerReservationResponse = partnerConnectorProxy.createReserve(partnerReservationRequest, transactionId);
            }

            if (this.isNewOrder(order)) {
                order = reservationMapper.toOrderEntity(request, partnerReservationResponse, transactionId, customerId, channel, listPriceId);
            }

            var pricingCalculatePrice = this.priceOrder(request, partnerReservationResponse);
            this.setPrices(order, pricingCalculatePrice, listPriceId);

            this.orderService.save(order);

            MDC.put(STATUS, "SUCCESS");
            log.info("ReservationServiceImpl.createOrder - Order created Order: {} transactionId: {} listPriceId: {}", order, transactionId, listPriceId);
            MDC.clear();
            return reservationMapper.toReservationResponse(order, 15);
        } catch (OrderFlightException e) {
            throw e;
        } catch (Exception e) {
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), "Unknown error on create reservation!", e);
        }
    }

    private boolean isNewOrder(OrderEntity order) {
        return Objects.isNull(order);
    }

    private boolean hasPartnerReserveExpired(PartnerReservationResponse partnerReservationResponse) {
        return Objects.isNull(partnerReservationResponse);
    }

    private List<PricingCalculatePrice> priceOrder(ReservationRequest request, PartnerReservationResponse partnerReservationResponse) {
        var pricingCalculateRequest = PricingCalculateRequestMapper.toPricingCalculateRequest(partnerReservationResponse, request.getCommerceOrderId());
        var pricingCalculateResponse = pricingProxy.calculate(pricingCalculateRequest);

        return getPricingCalculateByCommerceOrderId(request.getCommerceOrderId(), pricingCalculateResponse);
    }

    private PartnerReservationResponse getPartnerOrder(String partnerOrderId, String transactionId, String partnerCode, List<String> segmentsPartnerIds) {
        var partnerReservationResponse = partnerConnectorProxy.getReservation(partnerOrderId, transactionId, partnerCode, segmentsPartnerIds);

        if (!PROCESSING.getCode().equals(partnerReservationResponse.getStatus().getCode())) {
            throw new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_PARTNER_RESERVATION_EXPIRED_BUSINESS_ERROR, null, null);
        }
        return partnerReservationResponse;
    }

    private List<PricingCalculatePrice> getPricingCalculateByCommerceOrderId(String commerceOrderId, List<PricingCalculateResponse> pricingCalculateResponses) {

        var pricingCalculate = pricingCalculateResponses.stream()
                .filter(pricing -> commerceOrderId.equals(pricing.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "ReservationServiceImpl.getPricingCalculateByCommerceOrderId - Order not found in pricing response. commerceOrderId: " + commerceOrderId)
                );

        return pricingCalculate.getPrices();
    }

    private void setPrices(OrderEntity order, List<PricingCalculatePrice> prices, String listPrice) {
        var clientPrice = prices.stream()
                .filter(price -> listPrice.equals(price.getPriceListId()))
                .findFirst()
                .orElseThrow(() ->
                        new OrderFlightException(
                                ORDER_FLIGHT_INTERNAL_ERROR,
                                null,
                                "ReservationServiceImpl.getPricingCalculateByCommerceOrderId - PriceListId not found in pricing calculate response. listPrice: " + listPrice
                        )
                );
        order.getPrice().setPointsAmount(BigDecimal.valueOf(clientPrice.getPointsAmount()));
        order.getPrice().setAccrualPoints(clientPrice.getAccrualPoints().doubleValue());
        order.getPrice().setAmount(clientPrice.getAmount());
        this.setOrderPriceDescription(order, clientPrice);

        this.setOrderItemsPrice(order, prices, listPrice);
    }

    private void setOrderItemsPrice(OrderEntity order, List<PricingCalculatePrice> prices, String listPrice) {
        var clientPrice = prices.stream()
                .filter(price -> listPrice.equals(price.getPriceListId()))
                .findFirst()
                .orElseThrow(() ->
                        new OrderFlightException(
                                ORDER_FLIGHT_INTERNAL_ERROR,
                                null,
                                "ReservationServiceImpl.getPricingCalculateByCommerceOrderId - PriceListId not found in pricing calculate response. listPrice: " + listPrice
                        )
                );

        order.getItems()
                .forEach(item -> {
                    if (!item.getSkuId().contains(TAX)) {
                        item.getPrice().setPointsAmount(clientPrice.getFlight().getPointsAmount());
                        item.getPrice().setAmount(clientPrice.getFlight().getAmount());

                        var pricesModalities = prices.stream()
                                .map(price -> PriceModalityEntity.builder()
                                        .amount(price.getFlight().getAmount())
                                        .pointsAmount(price.getFlight().getPointsAmount())
                                        .accrualPoints(price.getAccrualPoints().doubleValue())
                                        .priceListId(price.getPriceListId())
                                        .build())
                                .collect(Collectors.toSet());

                        item.getPrice().setPricesModalities(pricesModalities);
                    }
                    if (item.getSkuId().contains(TAX)) {
                        item.getPrice().setPointsAmount(clientPrice.getTaxes().getPointsAmount());
                        item.getPrice().setAmount(clientPrice.getTaxes().getAmount());

                        var pricesModalities = prices.stream()
                                .map(price -> PriceModalityEntity.builder()
                                        .amount(price.getTaxes().getAmount())
                                        .pointsAmount(price.getTaxes().getPointsAmount())
                                        .accrualPoints(price.getAccrualPoints().doubleValue())
                                        .priceListId(price.getPriceListId())
                                        .build())
                                .collect(Collectors.toSet());

                        item.getPrice().setPricesModalities(pricesModalities);
                    }
                });
    }

    private void setOrderPriceDescription(OrderEntity order, PricingCalculatePrice price) {
        for (PricingCalculateFlight pricingCalculateFlight : price.getPricesDescription().getFlights()) {
            order.getPrice().getOrdersPriceDescription()
                    .forEach(priceDescription -> {
                        if (pricingCalculateFlight.getPassengerType().equals(priceDescription.getType())) {
                            priceDescription.setPointsAmount(pricingCalculateFlight.getPointsAmount());
                            priceDescription.setAmount(pricingCalculateFlight.getAmount());
                        }
                    });
        }

        for (PricingCalculateTaxes tax : price.getPricesDescription().getTaxes()) {
            order.getPrice().getOrdersPriceDescription()
                    .forEach(priceDescription -> {
                        if (tax.getType().equals(priceDescription.getType())) {
                            priceDescription.setAmount(tax.getAmount());
                            priceDescription.setPointsAmount(tax.getPointsAmount());
                        }
                    });
        }
    }


    private boolean isSameOrderItems(ReservationRequest request, Optional<OrderEntity> orderOptional) {
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
            return false;
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
                    "ReservationServiceImpl.hasSameTokens - Partner tokens are different!", null);
        }
    }
}

