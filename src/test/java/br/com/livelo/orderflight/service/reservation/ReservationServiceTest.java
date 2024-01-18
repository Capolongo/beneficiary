package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.ReservationItem;
import br.com.livelo.orderflight.domain.dto.ReservationRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.orderflight.mappers.*;
import br.com.livelo.orderflight.proxy.PartnerConnectorProxy;
import br.com.livelo.orderflight.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        var partnerReservationResponseMock = mock(PartnerReservationResponse.class);
        var orderMock = mock(OrderEntity.class);
        var requestMock = mock(ReservationRequest.class);
        when(orderService.findByCommerceOrderId(requestMock.getCommerceOrderId())).thenReturn(Optional.empty());
        when(partnerConnectorProxy.reservation(any(), anyString())).thenReturn(partnerReservationResponseMock);
        when(orderService.save(any())).thenReturn(orderMock);
        var transactionId = "123";

        var response = reservationService.createOrder(requestMock, transactionId, "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
    }

    @Test
    void shouldntCreateOrder_WhenOrderItemsQuantityDiverge() {
        var request = ReservationRequest.builder()
                .items(
                        List.of(
                                this.buildReservationItem("123", "teste"),
                                this.buildReservationItem("123", "teste"),
                                this.buildReservationItem("123", "teste")
                        )
                ).build();

        var order = OrderEntity.builder()
                .items(
                        Set.of(
                                this.buildOrderItem(1L, "123", "asdf"),
                                this.buildOrderItem(2L, "123", "asdf")
                        )
                ).build();

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var exception = assertThrows(ReservationException.class, () -> this.reservationService.createOrder(request, "123", "123", "WEB", "price"));
        assertEquals(ReservationErrorType.ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR, exception.getReservationErrorType());
    }

    @Test
    void shouldCreateOrder_WhenOrderItemsQuantityEqualsAndTokensEquals() {
        var transactionId = "123";
        var partnerReservationResponse = PartnerReservationResponse.builder().items(List.of(PartnerReservationItem.builder().type("teste").build())).build();
        when(partnerConnectorProxy.reservation(any(), anyString())).thenReturn(partnerReservationResponse);

        var request = ReservationRequest.builder()
                .items(
                        List.of(
                                this.buildReservationItem("123", "teste"),
                                this.buildReservationItem("123", "teste")
                        )
                ).segmentsPartnerIds(List.of("asdf", "asdf")).build();

        var order = OrderEntity.builder()
                .items(
                        Set.of(
                                this.buildOrderItem(1L, "123", "asdf"),
                                this.buildOrderItem(2L, "123", "asdf")
                        )
                ).build();

        when(orderService.findByCommerceOrderId(request.getCommerceOrderId())).thenReturn(Optional.of(order));
        var response = this.reservationService.createOrder(request, transactionId, "123", "WEB", "price");
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(transactionId, response.transactionId())
        );
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
