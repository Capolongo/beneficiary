package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorReservationResponse;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorReservationStatus;
import br.com.livelo.orderflight.domain.dtos.pricing.response.*;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.PriceCalculateRequestMapper;
import br.com.livelo.orderflight.mappers.ReservationMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.proxies.PricingProxy;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.reservation.impl.ReservationServiceImpl;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        var priceCalculateRequestMapper = Mappers.getMapper(PriceCalculateRequestMapper.class);
        this.reservationService = new ReservationServiceImpl(orderService, connectorPartnersProxy, pricingProxy, reservationMapper, priceCalculateRequestMapper);
    }

    @Test
    void shouldCreateReservation() {
        var partnerReservationResponse = buildPartnerReservationResponse();
        var orderMock = mock(OrderEntity.class);
        var requestMock = mock(ReservationRequest.class);
        when(requestMock.getCommerceOrderId()).thenReturn("QWERT");
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
    void shouldCreateOrder_WhenReservationExistsInPartner() {
        var transactionId = "123";
        var id = 1L;
        var type = "type_flight";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);
        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "type_flight_tax", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );
        var connectorReservationResponse = mock(ConnectorReservationResponse.class);
        var connectorReservationStatus = mock(ConnectorReservationStatus.class);

        when(connectorPartnersProxy.getReservation(any(), any(), any())).thenReturn(connectorReservationResponse);
        when(connectorReservationResponse.getStatus()).thenReturn(connectorReservationStatus);
        when(connectorReservationStatus.getCode()).thenReturn("LIVPNR-1007");
        when(orderService.save(any())).thenReturn(orderMock);


        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "cvc_flight"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "cvc_flight_tax")), transactionId);

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
        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));
        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, commerceItemId, segmentsPartnersId, "cvc_flight")), transactionId);
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

        var partnerReservationResponse = buildPartnerReservationResponse();
        when(connectorPartnersProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        when(pricingProxy.calculate(any())).thenReturn(buildPricingCalculateResponse());
        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(
                Set.of(
                        this.buildOrderItem(1L, transactionId, segmentsPartnersId, "cvc_flight"),
                        this.buildOrderItem(2L, transactionId, segmentsPartnersId, "cvc_flight_tax")
                ),
                transactionId
        );

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var response = this.reservationService.createOrder(request, "123", "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
    }

    @Test
    void shouldntCreateOrder_WhenTokensDifferent() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdfg";
        var token = "asdf";
        var id = 1L;

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token, "cvc_flight")), transactionId);

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

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token, "cvc_flight")), transactionId);

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenUnknownException() {
        var transactionId = "123";
        var type = "type_flight";
        var segmentsPartnersId = "asdfg";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenThrow(PersistenceException.class);
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }

    private ReservationRequest buildResevationRequest(List<ReservationItem> reservationItems, List<String> segmentsPartnersId) {
        return ReservationRequest.builder()
                .commerceOrderId("QWERT")
                .items(reservationItems)
                .segmentsPartnerIds(segmentsPartnersId)
                .build();
    }

    private OrderEntity buildOrderEntity(Set<OrderItemEntity> orderItems, String transactionId) {
        return OrderEntity.builder()
                .partnerCode("CVC")
                .transactionId(transactionId)
                .price(OrderPriceEntity.builder().partnerAmount(BigDecimal.TEN).build())
                .items(orderItems)
                .build();
    }

    private OrderItemEntity buildOrderItem(Long id, String commerceItemId, String token, String skuId) {
        return OrderItemEntity.builder()
                .id(id)
                .skuId(skuId)
                .productId("flight")
                .travelInfo(TravelInfoEntity.builder()
                        .adultQuantity(1)
                        .childQuantity(0)
                        .babyQuantity(0)
                        .type("test")
                        .build())
                .commerceItemId(commerceItemId)
                .segments(Set.of(SegmentEntity.builder()
                        .step("1")
                        .stops(1)
                        .flightDuration(30)
                        .flightsLegs(Set.of(FlightLegEntity.builder()
                                .airline(AirlineEntity.builder()
                                        .managedBy(AirlineManagedByEntity.builder().iata("").description("").build())
                                        .operatedBy(AirlineOperatedByEntity.builder().iata("").description("").build())
                                        .build())
                                .build()))
                        .luggages(Set.of(LuggageEntity.builder().build()))
                        .cancelationRules(Set.of(CancelationRuleEntity.builder().build()))
                        .changeRules(Set.of(ChangeRuleEntity.builder().build()))
                        .partnerId(token
                        ).build()))
                .price(OrderItemPriceEntity.builder().partnerAmount(BigDecimal.TEN).build())
                .build();
    }

    private ReservationItem buildReservationItem(String commerceItemId, String type, String skuId) {
        return ReservationItem.builder().skuId(skuId).commerceItemId(commerceItemId).productType(type).build();
    }

    private List<PricingCalculateResponse> buildPricingCalculateResponse() {
        return List.of(
                PricingCalculateResponse.builder()
                        .id("QWERT")
                        .prices(
                                List.of(
                                        PricingCalculatePrice.builder()
                                                .priceListId("price")
                                                .flight(PricingCalculateFlight.builder().amount(BigDecimal.TEN).pointsAmount(BigDecimal.TEN).build())
                                                .taxes(PricingCalculateTaxes.builder().amount(BigDecimal.TEN).pointsAmount(BigDecimal.TEN).build())
                                                .pricesDescription(
                                                        PricingCalculatePricesDescription.builder()
                                                                .flights(List.of(
                                                                        PricingCalculateFlight.builder()
                                                                                .passengerType("BY_ADULT")
                                                                                .pointsAmount(BigDecimal.TEN)
                                                                                .build()
                                                                ))
                                                                .taxes(
                                                                        List.of(
                                                                                PricingCalculateTaxes.builder()
                                                                                        .type("TESTE_TAX")
                                                                                        .amount(BigDecimal.TEN)
                                                                                        .pointsAmount(BigDecimal.TEN)
                                                                                        .build()
                                                                        )
                                                                )
                                                                .build()
                                                ).taxes(PricingCalculateTaxes.builder().amount(BigDecimal.TEN).pointsAmount(BigDecimal.TEN).build())
                                                .flight(PricingCalculateFlight.builder().amount(BigDecimal.TEN).pointsAmount(BigDecimal.TEN).build())
                                                .build()
                                )
                        ).build());
    }

    private PartnerReservationResponse buildPartnerReservationResponse() {
        return PartnerReservationResponse.builder()
                .commerceOrderId("QWERT")
                .ordersPriceDescription(
                        PartnerReservationOrdersPriceDescription.builder()
                                .flights(List.of(
                                                PartnerReservationOrdersPriceDescriptionFlight.builder()
                                                        .amount(new BigDecimal(10))
                                                        .passengerType("BY_ADULT")
                                                        .build()
                                        )
                                )
                                .taxes(
                                        List.of(PartnerReservationOrdersPriceDescriptionTaxes.builder()
                                                .amount(new BigDecimal(10))
                                                .type("TESTE_TAX")
                                                .build()
                                        )
                                )
                                .build()
                ).items(
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
                                                .flightLegs(List.of(PartnerReservationFlightsLeg
                                                        .builder()
                                                        .airline(PartnerReservationFlightLegAirline.builder()
                                                                .operatedBy(PartnerReservationAirline.builder().build())
                                                                .managedBy(PartnerReservationAirline.builder().build())
                                                                .build())
                                                        .build()))
                                                .build()))
                                        .build()
                        )
                )
                .build();
    }
}
