package br.com.livelo.orderflight.mock;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderPriceRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;

public class MockBuilder {
    public static ConfirmOrderRequest confirmOrderRequest() {
        Set<ConfirmOrderItemRequest> items = new HashSet<>();
        items.add(confirmOrderItemRequest());
        return ConfirmOrderRequest.builder()
                .id("id")
                .commerceOrderId("commerceOrderId")
                .commerceItemId("commerceItemId")
                .partnerCode("CVC")
                .partnerOrderId("partnerOrderId")
                .submittedDate("12-12-2024")
                .channel("channel")
                .originOfOrder("originOfOrder")
                .resubmission(Boolean.TRUE)
                .price(confirmOrderPriceRequest())
                .items(items)
                .build();
    }

    public static ConfirmOrderPriceRequest confirmOrderPriceRequest() {
        return ConfirmOrderPriceRequest.builder()
                .pointsAmount(BigDecimal.valueOf(1000))
                .build();
    }

    public static ConfirmOrderItemRequest confirmOrderItemRequest() {
        return ConfirmOrderItemRequest.builder()
                .commerceItemId("commerceItemId")
                .skuId("FLIGHT")
                .productId("productId")
                .pointsAmount(BigDecimal.valueOf(1000))
                .quantity(1)
                .externalCoupon("externalCoupon")
                .build();
    }

    public static ConfirmOrderResponse confirmOrderResponse() {
        return ConfirmOrderResponse
                .builder()
                .id("id")
                .commerceOrderId("commerceItemId")
                .partnerCode("partnerCode")
                .submittedDate("date")
                .expirationDate("Date")
                .transactionId("transactionId")
                .status(null)
                .price(null)
                .items(null)
                .build();
    }

    public static ConnectorConfirmOrderRequest connectorConfirmOrderRequest() {
        return ConnectorConfirmOrderRequest
                .builder()
                .id("id")
                .commerceOrderId("commerceOrderId")
                .commerceItemId("commerceItemId")
                .partnerOrderId("partnerOrderId")
                .partnerCode("partnerCode")
                .submittedDate("date")
                .paxs(null)
                .expirationDate("Date")
                .build();
    }

    public static ResponseEntity<ConnectorConfirmOrderResponse> connectorConfirmOrderResponse() {
        return ResponseEntity.ok().body(ConnectorConfirmOrderResponse
                .builder()
                .partnerOrderId("partnerOrderId")
                .partnerCode("partnerCode")
                .submittedDate("date")
                .expirationDate("Date")
                .transactionId("transactionId")
                .currentStatus(null)
                .voucher(null)
                .build());
    }

    public static OrderEntity orderEntity() {
        Set<OrderItemEntity> items = new HashSet<>();
        items.add(orderItemEntity());

        Set<OrderStatusEntity> statusHistory = new HashSet<>();
        statusHistory.add(statusInitial());

        return OrderEntity.builder()
                .id("id")
                .commerceOrderId("commerceOrderId")
                .partnerOrderId("partnerOrderId")
                .partnerCode("partnerCode")
                .submittedDate(LocalDateTime.now())
                .channel("channel")
                .tierCode("tierCode")
                .originOrder("originOrder")
                .customerIdentifier("customerIdentifier")
                .transactionId("transactionId")
                .expirationDate(LocalDateTime.now())
                .price(orderPriceEntity())
                .items(items)
                .statusHistory(statusHistory)
                .currentStatus(statusInitial())
                .build();
    }

    public static OrderItemEntity orderItemEntity() {
        Set<SegmentEntity> segments = new HashSet<>();
        segments.add(segmentEntity());

        return OrderItemEntity.builder()
                .id(1L)
                .commerceItemId("commerceItemId")
                .skuId("skuId")
                .productId("productId")
                .quantity(1)
                .externalCoupon("externalCoupon")
                .price(orderItemPriceEntity())
                .travelInfo(travelInfo())
                .segments(segments)
                .build();
    }

    public static OrderItemPriceEntity orderItemPriceEntity() {
        return OrderItemPriceEntity.builder()
                .id(1L)
                .listPrice("listPrice")
                .amount(BigDecimal.valueOf(1000))
                .pointsAmount(BigDecimal.valueOf(1000))
                .accrualPoints(BigDecimal.valueOf(1000))
                .partnerAmount(BigDecimal.valueOf(1000))
                .priceListId("priceListId")
                .build();
    }

    public static TravelInfoEntity travelInfo() {
        return TravelInfoEntity.builder().build();
    }

    public static SegmentEntity segmentEntity() {
        return SegmentEntity.builder().build();
    }

    public static OrderPriceEntity orderPriceEntity() {
        Set<OrderPriceDescriptionEntity> orderPriceDescriptions = new HashSet<>();
        orderPriceDescriptions.add(orderPriceDescriptionEntity());
        return OrderPriceEntity.builder()
                .id(1L)
                .accrualPoints(1000.0)
                .amount(BigDecimal.valueOf(1000))
                .pointsAmount(BigDecimal.valueOf(1000))
                .partnerAmount(BigDecimal.valueOf(1000))
                .priceListId("priceListId")
                .ordersPriceDescription(orderPriceDescriptions)
                .build();
    }

    public static OrderPriceDescriptionEntity orderPriceDescriptionEntity() {
        return OrderPriceDescriptionEntity.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .pointsAmount(BigDecimal.valueOf(1000))
                .type("type")
                .description("desc")
                .build();
    }

    public static OrderStatusEntity statusInitial() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1006")
                .description("desc")
                .partnerCode("partnerCode")
                .partnerDescription("desc")
                .partnerResponse("response")
                .statusDate(LocalDateTime.now())
                .build();
    }

    public static OrderStatusEntity statusProcessing() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1007")
                .description("desc")
                .partnerCode("partnerCode")
                .partnerDescription("desc")
                .partnerResponse("response")
                .statusDate(LocalDateTime.now())
                .build();
    }

    public static OrderStatusEntity statusFailed() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1014")
                .description("desc")
                .partnerCode("partnerCode")
                .partnerDescription("desc")
                .partnerResponse("response")
                .statusDate(LocalDateTime.now())
                .build();
    }
}
