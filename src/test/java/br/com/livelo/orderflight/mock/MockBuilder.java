package br.com.livelo.orderflight.mock;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderPriceRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderItemResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderPriceResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
                .resubmission(Boolean.FALSE)
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
                .partnerOrderId("partnerOrderId")
                .commerceOrderId("commerceOrderId")
                .partnerCode("partnerCode")
                .submittedDate("date")
                .expirationDate("Date")
                .transactionId("transactionId")
                .status(confirmOrderStatusResponse())
                .price(confirmOrderPriceResponse())
                .items(Set.of(confirmOrderItemResponse()))
                .build();
    }

    public static ConfirmOrderStatusResponse confirmOrderStatusResponse() {
        return ConfirmOrderStatusResponse.builder()
                .code("LIVPNR-1006")
                .description("description")
                .details("partnerDescription")
                .build();
    }

    public static ConfirmOrderPriceResponse confirmOrderPriceResponse() {
        return ConfirmOrderPriceResponse.builder()
                .amount(BigDecimal.valueOf(1000))
                .pointsAmount(BigDecimal.valueOf(1000))
                .build();
    }

    public static ConfirmOrderItemResponse confirmOrderItemResponse() {
        return ConfirmOrderItemResponse.builder()
                .commerceItemId("commerceItemId")
                .skuId("skuId")
                .productId("productId")
                .quantity(1)
                .price(confirmOrderPriceResponse())
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
                .paxs(List.of(connectorConfirmOrderPaxRequest()))
                .expirationDate("Date")
                .build();
    }

    private static ConnectorConfirmOrderPaxRequest connectorConfirmOrderPaxRequest() {
        return ConnectorConfirmOrderPaxRequest.builder()
                .type("type")
                .firstName("firstName")
                .lastName("lastName")
                .gender("gender")
                .birthDate("birthDate")
                .document("document")
                .documentType("documentType")
                .email("email")
                .areaCode("areaCode")
                .phone("phone")
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

    public static OrderEntity orderEntityAlreadyConfirmed() {
        Set<OrderItemEntity> items = new HashSet<>();
        items.add(orderItemEntity());

        Set<OrderStatusEntity> statusHistory = new HashSet<>();
        statusHistory.add(statusInitial());
        statusHistory.add(statusProcessing());

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
                .currentStatus(statusProcessing())
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
        return TravelInfoEntity.builder()
                .id(1L)
                .type("type")
                .reservationCode("reservation")
                .paxs(Set.of(paxEntity()))
                .build();
    }

    public static PaxEntity paxEntity() {
        return PaxEntity.builder()
                .id(1L)
                .type("type")
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .areaCode("areaCode")
                .phoneNumber("phone")
                .gender("gender")
                .birthDate("birthDate")
                .document("document")
                .documentType("documentType")
                .build();
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
                .description("description")
                .build();
    }

    public static OrderStatusEntity statusInitial() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1006")
                .description("description")
                .partnerCode("partnerCode")
                .partnerDescription("partnerDescription")
                .partnerResponse("partnerResponse")
                .statusDate(LocalDateTime.now())
                .build();
    }

    public static OrderStatusEntity statusProcessing() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1007")
                .description("description")
                .partnerCode("partnerCode")
                .partnerDescription("partnerDescription")
                .partnerResponse("response")
                .statusDate(LocalDateTime.now())
                .build();
    }

    public static OrderStatusEntity statusFailed() {
        return OrderStatusEntity.builder()
                .id(1L)
                .code("LIVPNR-1014")
                .description("description")
                .partnerCode("partnerCode")
                .partnerDescription("partnerDescription")
                .partnerResponse("response")
                .statusDate(LocalDateTime.now())
                .build();
    }

    public static ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse() {
        return ConnectorConfirmOrderStatusResponse.builder()
                .id(1L)
                .code("LIVPNR-1006")
                .description("description")
                .partnerCode("partnerCode")
                .partnerDescription("partnerDescription")
                .partnerResponse("partnerResponse")
                .statusDate(LocalDateTime.now())
                .build();
    }
}
