package br.com.livelo.orderflight.mock;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderPriceRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;

import java.math.BigDecimal;
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
                .pointsAmount(BigDecimal.valueOf(20000))
                .build();
    }

    public static ConfirmOrderItemRequest confirmOrderItemRequest() {
        return ConfirmOrderItemRequest.builder()
                .commerceItemId("commerceItemId")
                .skuId("FLIGHT")
                .productId("productId")
                .pointsAmount(BigDecimal.valueOf(20000))
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
}
