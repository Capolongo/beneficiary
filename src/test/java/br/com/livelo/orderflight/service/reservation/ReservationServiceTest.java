package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.proxies.PricingProxy;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;
import br.com.livelo.orderflight.mappers.ReservationMapper;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private ReservationServiceImpl reservationService;
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private ConnectorPartnersProxy connectorPartnersProxy;
    @Mock
    private PricingProxy pricingProxy;

    @BeforeEach
    void setup() {
        var reservationMapper = Mappers.getMapper(ReservationMapper.class);
        this.reservationService = new ReservationServiceImpl(orderService, connectorPartnersProxy, pricingProxy, reservationMapper);
    }

    @Test
    void shouldCreateReservation() {
        var partnerReservationResponse = buildPartnerReservationResponse();
        var orderMock = mock(OrderEntity.class);
        var requestMock = mock(ReservationRequest.class);
        when(orderService.findByCommerceOrderId(requestMock.getCommerceOrderId())).thenReturn(Optional.empty());
        when(connectorPartnersProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);
        when(pricingProxy.calculate(any())).thenReturn(buildPricingCalculateResponse());
        var transactionId = "123";

        var response = reservationService.createOrder(requestMock, transactionId, "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
              );
    }

    @Test
    void shouldCreateOrder_WhenOrderItemsQuantityEqualsAndTokensEquals() {
        var transactionId = "123";
        var id = 1L;
        var type = "type_flight";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);

        var partnerReservationResponse = buildPartnerReservationResponse();

        when(connectorPartnersProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        when(pricingProxy.calculate(any())).thenReturn(buildPricingCalculateResponse());
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
    }

    @Test
    void shouldCreateOrder_WhenOrderExistsAndCommerceItemsDifferent() {
        var transactionId = "1234";
        var id = 1L;
        var commerceItemId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdf";

        var partnerReservationResponse = buildPartnerReservationResponse();
        when(pricingProxy.calculate(any())).thenReturn(buildPricingCalculateResponse());
        when(connectorPartnersProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));
        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, commerceItemId, segmentsPartnersId)));
        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
    }

    @Test
    void shouldntCreateOrder_WhenOrderItemsQuantityDiverge() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdf";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(
                Set.of(
                        this.buildOrderItem(1L, transactionId, segmentsPartnersId),
                        this.buildOrderItem(2L, transactionId, segmentsPartnersId)
                )
        );

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenTokensDifferent() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdfg";
        var token = "asdf";
        var id = 1L;

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenQuantityTokensDifferent() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdfg";
        var token = "asdf";
        var id = 1L;

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenUnknownException() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdfg";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenThrow(PersistenceException.class);
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }

    private ReservationRequest buildResevationRequest(List<ReservationItem> reservationItems, List<String> segmentsPartnersId) {
        return ReservationRequest.builder()
                .items(reservationItems)
                .segmentsPartnerIds(segmentsPartnersId)
                .build();
    }

    private OrderEntity buildOrderEntity(Set<OrderItemEntity> orderItems) {
        return OrderEntity.builder()
                .items(orderItems)
                .build();
    }

    private OrderItemEntity buildOrderItem(Long id, String commerceItemId, String token) {
        return OrderItemEntity.builder()
                .id(id)
                .commerceItemId(commerceItemId)
                .segments(Set.of(SegmentEntity.builder().partnerId(token).build()))
                .build();
    }

    private ReservationItem buildReservationItem(String commerceItemId, String type) {
        return ReservationItem.builder().commerceItemId(commerceItemId).productType(type).build();
    }

    private PricingCalculateResponse[] buildPricingCalculateResponse(){
        return new PricingCalculateResponse[]{PricingCalculateResponse.builder()
                .prices(
                        new ArrayList<>(List.of(PricingCalculatePrice.builder().priceListId("price")
                                .pricesDescription( new ArrayList<>(List.of(PricingCalculatePricesDescription.builder()
                                        .passengerType("BY_ADULT").pointsAmount(7)
                                        .taxes(new ArrayList<>(List.of(PricingCalculateTaxes.builder()
                                                .description("TESTE_TAX")
                                                .pointsAmount(7)
                                                .build())))
                                        .build()))).build()))
                ).build()};
    }

    private PartnerReservationResponse buildPartnerReservationResponse(){
        return PartnerReservationResponse.builder()
                .ordersPriceDescription(
                        List.of(PartnerReservationOrdersPriceDescription.builder()
                                .amount(new BigDecimal(10))
                                .type("BY_ADULT")
                                .taxes(
                                        List.of(PartnerReservationOrdersPriceDescriptionTaxes.builder()
                                                .amount(new BigDecimal(10))
                                                .description("TESTE_TAX")
                                                .build())).build())
                )
                .items(
                        List.of(
                                PartnerReservationItem
                                        .builder()
                                        .type("type_flight")
                                        .travelInfo(PartnerReservationTravelInfo.builder().build())
                                        .segments(List.of(PartnerReservationSegment.builder()
                                                .step("1")
                                                .stops(1)
                                                .flightDuration(55)
                                                .luggages(List.of(PartnerReservationLuggage.builder().build()))
                                                .cancelationRules(List.of(PartnerReservationCancelationRule
                                                        .builder()
                                                        .build()))
                                                .changeRules(List.of(PartnerReservationChangeRule
                                                        .builder()
                                                        .build()))
                                                .flightsLegs(List.of(PartnerReservationFlightsLeg
                                                        .builder()
                                                        .build()))
                                                .build()))
                                        .build()
                        )
                )
                .build();
    }
}
