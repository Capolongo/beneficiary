package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.response.*;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.mappers.PricingCalculateRequestMapper;
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
import java.util.*;
import java.util.stream.Collectors;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_ORDER_STATUS_INVALID_BUSINESS_ERROR;
import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_PARTNER_RESERVATION_EXPIRED_BUSINESS_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        var pricingCalculateRequestMapper = Mappers.getMapper(PricingCalculateRequestMapper.class);
        this.reservationService = new ReservationServiceImpl(orderService, connectorPartnersProxy, pricingProxy, reservationMapper, pricingCalculateRequestMapper);
    }

    @Test
    void shouldCreateReservation() {
        var transactionId = "123";
        var partnerReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", "ci123");
        var orderMock = mock(OrderEntity.class);
        var requestMock = mock(ReservationRequest.class);
        var ids = requestMock.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(requestMock.getCommerceOrderId());
        when(requestMock.getCommerceOrderId()).thenReturn("QWERT");
        when(requestMock.getPartnerCode()).thenReturn("CVC");
        when(requestMock.getSegmentsPartnerIds()).thenReturn(List.of("fdf"));
        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(any())).thenReturn(Optional.empty());

        when(connectorPartnersProxy.createReserve(anyString(), any(), anyString(), anyString())).thenReturn(partnerReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());

        var response = reservationService.createOrder(requestMock, transactionId, "123", "WEB", "price", "");

        assertNotNull(response);
    }

    @Test
    void shouldCreateOrder_WhenReservationExistsInPartner() {
        var transactionId = "123";
        var id = 1L;
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);

        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "type_flight_tax", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );
        var connectorReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", segmentsPartnersId);

        when(connectorPartnersProxy.getReservation(any(), any(), any(), anyString())).thenReturn(connectorReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);


        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "CVCFLIGHT"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")),
                transactionId,
                "LIVPNR-1006"
        );

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", "");
        assertNotNull(response);
    }

    @Test
    void shouldCreateOrder_WhenReservationExistsInPartnerAndItemIsFlight() {
        var transactionId = "123";
        var id = 1L;
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);

        when(orderService.isFlightItem(any())).thenReturn(true);
        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "type_flight_tax", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );
        var connectorReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", segmentsPartnersId);
        connectorReservationResponse.getItems().getFirst().getTravelInfo().setIsInternational(true);
        when(connectorPartnersProxy.getReservation(any(), any(), any(), anyString())).thenReturn(connectorReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);


        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "CVCFLIGHT"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")),
                transactionId,
                "LIVPNR-1006"
        );

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", "");
        assertNotNull(response);
    }

    @Test
    void shouldCreateOrder_WhenReservationExistsInPartnerAndPriceModalitiesIsNull() {
        var transactionId = "123";
        var id = 1L;
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);

        when(orderService.isFlightItem(any())).thenReturn(true);
        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "FLIGHT_TAX", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );
        var connectorReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", segmentsPartnersId);

        when(connectorPartnersProxy.getReservation(any(), any(), any(), any())).thenReturn(connectorReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);


        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "CVCFLIGHT"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")),
                transactionId,
                "LIVPNR-1006"
        );

        order.getItems().forEach(item -> item.getPrice().setPricesModalities(null));

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", "");
        assertNotNull(response);
    }

    @Test
    void shouldntCreateOrder_WhenReservationExpiredInPartner() {
        var transactionId = "123";
        var id = 1L;
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";
        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "type_flight_tax", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );
        var connectorReservationResponse = buildPartnerReservationResponse("LIVPNR-9001", segmentsPartnersId);

        when(connectorPartnersProxy.getReservation(any(), any(), any(), anyString())).thenReturn(connectorReservationResponse);


        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "CVCFLIGH"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")),
                transactionId,
                "LIVPNR-1006"
        );

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(orderService.save(any())).thenReturn(order);
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", ""));
        assertAll(
                () -> assertEquals(ORDER_FLIGHT_PARTNER_RESERVATION_EXPIRED_BUSINESS_ERROR, exception.getOrderFlightErrorType()),
                () -> verify(orderService, times(1)).save(any())
        );
    }

    @Test
    void shouldntCreateOrder_WhenOrderExistsInDB_AndStatusIsNotInitial() {
        var transactionId = "123";
        var id = 1L;
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";
        var request = this.buildResevationRequest(
                List.of(this.buildReservationItem(transactionId, type, "cvc_flight"),
                        this.buildReservationItem(transactionId, "type_flight_tax", "cvc_flight_tax")
                ),
                List.of(segmentsPartnersId, segmentsPartnersId)
        );


        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId, "CVCFLIGHT"), this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")),
                transactionId,
                "LIVPNR-9001"
        );
        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));

        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", ""));
        assertEquals(ORDER_FLIGHT_ORDER_STATUS_INVALID_BUSINESS_ERROR, exception.getOrderFlightErrorType());
    }

    @Test
    void shouldCreateOrder_WhenOrderExistsAndCommerceItemsDifferent() {
        var transactionId = "1234";
        var id = 1L;
        var commerceItemId = "123";
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";

        var partnerReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", segmentsPartnersId);
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());
        when(connectorPartnersProxy.createReserve(anyString(), any(), anyString(), anyString())).thenReturn(partnerReservationResponse);
        var request = this.buildResevationRequest(Collections.singletonList(this.buildReservationItem(segmentsPartnersId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));
        var order = this.buildOrderEntity(request.getCommerceOrderId(), Set.of(this.buildOrderItem(id, commerceItemId, segmentsPartnersId, "CVCFLIGHT")), transactionId, "LIVPNR-1006");

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());
        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(orderService.save(any())).thenReturn(order);
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price", "");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
    }

    @Test
    void shouldCreateNewOrder_WhenOrderItemsQuantityDiverge() {
        var transactionId = "123";
        var type = "FLIGHT";
        var segmentsPartnersId = "asdf";

        var partnerReservationResponse = buildPartnerReservationResponse("LIVPNR-1006", segmentsPartnersId);
        when(connectorPartnersProxy.createReserve(anyString(), any(), anyString(), anyString())).thenReturn(partnerReservationResponse);
        when(pricingProxy.calculate(any(), anyString(), anyString())).thenReturn(buildPricingCalculateResponse());
        var request = this.buildResevationRequest(Collections.singletonList(this.buildReservationItem(segmentsPartnersId, type, "CVCFLIGHT")), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(
                request.getCommerceOrderId(),
                Set.of(
                        this.buildOrderItem(1L, transactionId, segmentsPartnersId, "CVCFLIGHT"),
                        this.buildOrderItem(2L, transactionId, segmentsPartnersId, "CVCFLIGHTTAX")
                ),
                transactionId,
                "LIVPNR-1006"
        );

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenReturn(Optional.of(order));
        when(orderService.save(any())).thenReturn(order);
        var response = this.reservationService.createOrder(request, "123", "123", "WEB", "price", "");

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId()),
                () -> verify(orderService, times(1)).delete(any())
        );
    }

    @Test
    void shouldntCreateOrder_WhenUnknownException() {
        var transactionId = "123";
        var type = "FLIGHT";
        var segmentsPartnersId = "asdfg";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type, "cvc_flight")), List.of(segmentsPartnersId, segmentsPartnersId));

        var ids = request.getItems()
                .stream()
                .map(ReservationItem::getCommerceItemId)
                .collect(Collectors.toList());
        ids.add(request.getCommerceOrderId());

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(ids)).thenThrow(PersistenceException.class);
        var exception = assertThrows(OrderFlightException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price", ""));
        assertEquals(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getOrderFlightErrorType());
    }

    private ReservationRequest buildResevationRequest(List<ReservationItem> reservationItems, List<String> segmentsPartnersId) {
        return ReservationRequest.builder()
                .commerceOrderId("QWERT")
                .partnerCode("CVC")
                .items(reservationItems)
                .segmentsPartnerIds(segmentsPartnersId)
                .build();
    }

    private OrderEntity buildOrderEntity(String commerceOrderId, Set<OrderItemEntity> orderItems, String transactionId, String statusCode) {
        return OrderEntity.builder()
                .commerceOrderId(commerceOrderId)
                .partnerCode("CVC")
                .transactionId(transactionId)
                .currentStatus(OrderCurrentStatusEntity.builder().code(statusCode).build())
                .price(OrderPriceEntity.builder().partnerAmount(BigDecimal.TEN).ordersPriceDescription(Set.of(OrderPriceDescriptionEntity.builder().build())).build())
                .items(orderItems)
                .statusHistory(new HashSet<>())
                .build();
    }

    private OrderItemEntity buildOrderItem(Long id, String commerceItemId, String token, String skuId) {
        return OrderItemEntity.builder()
                .id(id)
                .skuId(skuId)
                .productId("flight")
                .productType("FLIGHT")
                .travelInfo(TravelInfoEntity.builder()
                        .adt(1)
                        .chd(0)
                        .inf(0)
                        .type("test")
                        .build())
                .commerceItemId(commerceItemId)
                .segments(Set.of(SegmentEntity.builder()
                        .step("1")
                        .stops(1)
                        .flightDuration(30)
                        .flightsLegs(Set.of(FlightLegEntity.builder()
                                .airlineManagedByIata("")
                                .airlineManagedByDescription("")
                                .airlineOperatedByIata("")
                                .airlineOperatedByDescription("")
                                .build()))
                        .luggages(Set.of(LuggageEntity.builder().build()))
                        .cancellationRules(Set.of(CancellationRuleEntity.builder().build()))
                        .changeRules(Set.of(ChangeRuleEntity.builder().build()))
                        .partnerId(token)
                        .build()))
                .price(OrderItemPriceEntity.builder().pricesModalities(Set.of(PriceModalityEntity.builder().priceListId("price").build())).partnerAmount(BigDecimal.TEN).build())
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
                                                .pointsAmount(BigDecimal.TEN)
                                                .accrualPoints(BigDecimal.TEN)
                                                .priceListId("price")
                                                .flight(PricingCalculateFlight.builder().amount(BigDecimal.TEN).pointsAmount(BigDecimal.TEN).passengerType("ADULT").build())
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

    private PartnerReservationResponse buildPartnerReservationResponse(String status, String commerceItemId) {
        return PartnerReservationResponse.builder()
                .commerceOrderId("QWERT")
                .partnerCode("CVC")
                .currentStatus(PartnerResponseStatus.builder().code(status).build())
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
                                        .type("FLIGHT")
                                        .amount(new BigDecimal(10))
                                        .travelInfo(PartnerReservationTravelInfo.builder().build())
                                        .segments(List.of(PartnerReservationSegment.builder()
                                                .step("1")
                                                .stops(1)
                                                .flightDuration(55)
                                                .luggages(List.of(PartnerReservationLuggage.builder().build()))
                                                .cancellationRules(List.of(PartnerReservationCancellationRule
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
                                        .build(),
                                PartnerReservationItem
                                        .builder()
                                        .type("type_flight_tax")
                                        .build()
                        )
                )
                .build();
    }
}
