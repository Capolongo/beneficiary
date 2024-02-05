package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.orderflight.mappers.ReservationMapperImpl;
import br.com.livelo.orderflight.proxy.PartnerConnectorProxy;
import br.com.livelo.orderflight.service.OrderService;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private ReservationService reservationService;
    @Mock
    private OrderService orderService;

    @Mock
    private PartnerConnectorProxy partnerConnectorProxy;


    @BeforeEach
    void setup() {
        var cartMapper = new ReservationMapperImpl();
        this.reservationService = new ReservationService(orderService, partnerConnectorProxy, cartMapper);
    }

    @Test
    void shouldCreateReservation() {
        var partnerReservationResponse = PartnerReservationResponse.builder()
                .items(
                        List.of(
                                PartnerReservationItem
                                        .builder()
                                        .segments(List.of(PartnerReservationSegment.builder().build()))
                                        .build()
                        )
                )
                .build();
        var orderMock = mock(OrderEntity.class);
        var requestMock = mock(ReservationRequest.class);
        when(orderService.findByCommerceOrderId(requestMock.getCommerceOrderId())).thenReturn(Optional.empty());
        when(partnerConnectorProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);
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
        var type = "teste";
        var segmentsPartnersId = "asdf";
        var orderMock = mock(OrderEntity.class);

        var partnerReservationResponse = PartnerReservationResponse.builder()
                .items(List.of(PartnerReservationItem
                        .builder()
                        .type("teste")
                                .segments(List.of(PartnerReservationSegment
                                        .builder()
                                                .luggages(List.of(PartnerReservationLuggage
                                                        .builder()
                                                        .build()))
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
                        .build()))
                .build();

        when(partnerConnectorProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);
        when(orderService.save(any())).thenReturn(orderMock);

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, segmentsPartnersId)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
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
        var type = "teste";
        var segmentsPartnersId = "asdf";

        var partnerReservationResponse = PartnerReservationResponse.builder()
                .items(List.of(PartnerReservationItem
                        .builder()
                        .type("teste")
                        .segments(List.of(PartnerReservationSegment
                                .builder()
                                .luggages(List.of(PartnerReservationLuggage
                                        .builder()
                                        .build()))
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
                        .build()))
                .build();
        when(partnerConnectorProxy.createReserve(any(), anyString())).thenReturn(partnerReservationResponse);

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
        var type = "teste";
        var segmentsPartnersId = "asdf";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(
                Set.of(
                        this.buildOrderItem(1L, transactionId, segmentsPartnersId),
                        this.buildOrderItem(2L, transactionId, segmentsPartnersId)
                )
        );

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(ReservationException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenTokensDifferent() {
        var transactionId = "123";
        var type = "teste";
        var segmentsPartnersId = "asdfg";
        var token = "asdf";
        var id = 1L;

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(ReservationException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenQuantityTokensDifferent() {
        var transactionId = "123";
        var type = "teste";
        var segmentsPartnersId = "asdfg";
        var token = "asdf";
        var id = 1L;

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId, segmentsPartnersId));

        var order = this.buildOrderEntity(Set.of(this.buildOrderItem(id, transactionId, token)));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(ReservationException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldntCreateOrder_WhenUnknownException() {
        var transactionId = "123";
        var type = "teste";
        var segmentsPartnersId = "asdfg";

        var request = this.buildResevationRequest(List.of(this.buildReservationItem(transactionId, type)), List.of(segmentsPartnersId, segmentsPartnersId));

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenThrow(PersistenceException.class);
        var exception = assertThrows(ReservationException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, exception.getReservationErrorType());
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
}
