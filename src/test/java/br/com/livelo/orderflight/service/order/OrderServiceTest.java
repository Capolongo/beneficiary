package br.com.livelo.orderflight.service.order;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.orderValidate.request.OrderValidateRequestDTO;
import br.com.livelo.orderflight.domain.dtos.orderValidate.response.OrderValidateResponseDTO;
import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.OrderProcessMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.repository.ItemRepository;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @Mock
    private OrderProcessMapper orderProcessMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldReturnFoundOrderById() throws OrderFlightException {
        Optional<OrderEntity> mockedOrder = Optional.of(MockBuilder.orderEntity());
        when(orderRepository.findById(anyString())).thenReturn(mockedOrder);
        OrderEntity order = orderService.getOrderById("id");
        assertEquals(mockedOrder.get(), order);
    }

    @Test
    void shouldThrowReservationExceptionWhenOrderNotFound() throws OrderFlightException {
        when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(OrderFlightException.class, () -> {
            orderService.getOrderById("id");
        });
    }

    @Test
    void shouldAddNewStatusToOrder() {
        OrderEntity order = MockBuilder.orderEntity();
        OrderCurrentStatusEntity status = MockBuilder.statusFailed();

        when(confirmOrderMapper
                .connectorConfirmOrderStatusResponseToStatusEntity(any(ConnectorConfirmOrderStatusResponse.class)))
                .thenReturn(status);

        orderService.addNewOrderStatus(order,
                confirmOrderMapper.connectorConfirmOrderStatusResponseToStatusEntity(
                        MockBuilder.connectorConfirmOrderStatusResponse()));

        assertEquals(status, order.getCurrentStatus());
    }

    @Test
    void shouldFindOrderByCommerceOrderIdIn() {
        var orderMock = mock(OrderEntity.class);
        when(this.orderRepository.findByCommerceOrderIdInAndExpirationDateAfter(any(), any())).thenReturn(Optional.of(orderMock));
        var response = this.orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("123"));

        assertAll(
                () -> assertTrue(response.isPresent()),
                () -> assertInstanceOf(OrderEntity.class, response.get()));
    }

    @Test
    void shouldntFindOrderByCommerceOrderIdIn() {
        when(this.orderRepository.findByCommerceOrderIdInAndExpirationDateAfter(any(), any())).thenReturn(Optional.empty());
        var response = this.orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("123"));

        assertTrue(!response.isPresent());
    }

    @Test
    void shouldDeleteOrder() {
        var orderMock = mock(OrderEntity.class);
        doNothing().when(orderRepository).delete(any(OrderEntity.class));
        this.orderService.delete(orderMock);

        verify(orderRepository, times(1)).delete(orderMock);
    }

    @Test
    void shouldSaveOrder() {
        var orderMock = mock(OrderEntity.class);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderMock);
        var response = this.orderService.save(orderMock);

        assertNotNull(response);
    }

    @Test
    void shouldReturnSuccessGetOrdersByStatusCodeAndLimitArrivalDate() throws OrderFlightException {
        ReflectionTestUtils.setField(orderService, "orderProcessMaxRows", 500);

        String statusCode = "LIVPNR-1030";
        String limitArrivalDate = "2000-01-01";
        int page = 1;
        int rows = 4;

        Page<OrderProcess> repositoryResponse = Page.empty();
        PaginationOrderProcessResponse mappedRepositoryResponse = MockBuilder.paginationOrderProcessResponse(page,
                rows);

        when(orderRepository.findAllByCurrentStatusCodeAndArrivalDateLessThan(anyString(), any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResponse);
        when(orderProcessMapper.pageRepositoryToPaginationResponse(any())).thenReturn(mappedRepositoryResponse);

        PaginationOrderProcessResponse response = orderService.getOrdersByStatusCode(statusCode,
                Optional.of(limitArrivalDate), page, rows);

        assertEquals(mappedRepositoryResponse, response);
        assertEquals(mappedRepositoryResponse.getOrders().size(), response.getRows());
        verify(orderRepository).findAllByCurrentStatusCodeAndArrivalDateLessThan(statusCode,
                LocalDate.parse(limitArrivalDate, DateTimeFormatter.ISO_DATE).atTime(0, 0),
                PageRequest.of(page - 1, rows));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnSuccessGetOrdersByStatusCode() throws OrderFlightException {
        ReflectionTestUtils.setField(orderService, "orderProcessMaxRows", 500);

        String statusCode = "LIVPNR-1006";
        int page = 1;
        int rows = 4;

        Page<OrderProcess> repositoryResponse = Page.empty();
        PaginationOrderProcessResponse mappedRepositoryResponse = MockBuilder.paginationOrderProcessResponse(page,
                rows);

        when(orderRepository.findAllByCurrentStatusCode(anyString(), any(Pageable.class)))
                .thenReturn(repositoryResponse);
        when(orderProcessMapper.pageRepositoryToPaginationResponse(any())).thenReturn(mappedRepositoryResponse);

        PaginationOrderProcessResponse response = orderService.getOrdersByStatusCode(statusCode, Optional.empty(), page, rows);

        assertEquals(mappedRepositoryResponse, response);
        assertEquals(mappedRepositoryResponse.getOrders().size(), response.getRows());
        verify(orderRepository).findAllByCurrentStatusCode(statusCode, PageRequest.of(page - 1, rows));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldReturnSuccessGetOrdersByStatusCodeWithRowsGraterThenMaxLimit() throws OrderFlightException {
        ReflectionTestUtils.setField(orderService, "orderProcessMaxRows", 500);

        String statusCode = "LIVPNR-1006";
        int page = 1;
        int rows = 600;

        Page<OrderProcess> repositoryResponse = Page.empty();
        PaginationOrderProcessResponse mappedRepositoryResponse = MockBuilder.paginationOrderProcessResponse(page,
                rows);

        when(orderRepository.findAllByCurrentStatusCode(anyString(), any(Pageable.class)))
                .thenReturn(repositoryResponse);
        when(orderProcessMapper.pageRepositoryToPaginationResponse(any())).thenReturn(mappedRepositoryResponse);

        PaginationOrderProcessResponse response = orderService.getOrdersByStatusCode(statusCode, Optional.empty(), page, rows);

        assertEquals(mappedRepositoryResponse, response);
        assertEquals(mappedRepositoryResponse.getOrders().size(), response.getRows());
        verify(orderRepository).findAllByCurrentStatusCode(statusCode, PageRequest.of(page - 1, 500));
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldUpdateVoucher() {
        String voucher = "any voucher";
        OrderItemEntity order = MockBuilder.orderItemEntity();
        orderService.updateVoucher(order, voucher);

        assertEquals(voucher, order.getTravelInfo().getVoucher());
    }

    @Test
    void shouldIncrementProcessCounter() {
        ProcessCounterEntity processCounter = MockBuilder.processCounterEntity(1, Webhooks.GETCONFIRMATION.value);
        orderService.incrementProcessCounter(processCounter);
        assertEquals(2, processCounter.getCount());
    }

    @Test
    void shouldReturnNewProcessCounter() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderEntity order = MockBuilder.orderEntity();
        order.setProcessCounters(new HashSet<>());

        ProcessCounterEntity processCounter = orderService.getProcessCounter(order, process);

        assertInstanceOf(ProcessCounterEntity.class, processCounter);
        assertEquals(MockBuilder.processCounterEntity(0, process), processCounter);
    }

    @Test
    void shouldReturnProcessCounter() {
        String process = Webhooks.GETCONFIRMATION.value;
        OrderEntity order = MockBuilder.orderEntity();
        order.setProcessCounters(Set.of(MockBuilder.processCounterEntity(12, process)));

        ProcessCounterEntity processCounter = orderService.getProcessCounter(order, process);

        assertInstanceOf(ProcessCounterEntity.class, processCounter);
        assertEquals(order.getProcessCounters().stream().findFirst().get(), processCounter);
    }

    @Test
    void shouldReturnSucessByCommerceItemIdAndSkuId() {

        when(itemRepository.findByCommerceItemIdAndSkuId(anyString(), anyString()))
                .thenReturn(Optional.of(MockBuilder.orderItemEntity()));

        OrderItemEntity orderItemEntity = orderService.findByCommerceItemIdAndSkuId("1",
                SkuItemResponse.builder().skuId("cvc_flight_tax").build());

        assertNotNull(orderItemEntity);
    }

    @Test
    void shouldReturnErrorOrderFlightExceptionByCommerceItemIdAndSkuId() {

        when(itemRepository.findByCommerceItemIdAndSkuId(anyString(), anyString())).thenReturn(Optional.empty());
        var skuItemResponse = SkuItemResponse.builder().skuId("cvc_flight_tax").build();

        assertThrows(OrderFlightException.class, () -> orderService.findByCommerceItemIdAndSkuId("1", skuItemResponse));
    }

    @Test
    void shouldReturnStatusFailed() {
        OrderCurrentStatusEntity statusFailed = orderService.buildOrderStatusFailed("any cause");

        assertInstanceOf(OrderCurrentStatusEntity.class, statusFailed);
    }

    @Test
    void shouldReturnOnlyFlightItemFromOrder() {
        Set<OrderItemEntity> flightItemMock = MockBuilder.orderEntity().getItems();

        OrderItemEntity flightItem = orderService.getFlightFromOrderItems(flightItemMock);

        assertInstanceOf(OrderItemEntity.class, flightItem);
        assertEquals(flightItemMock, Set.of(flightItem));
    }

    @Test
    void shouldThrowFlightItemFromOrderWhenNotFound() {
        Set<OrderItemEntity> flightItemMock = Set.of();

        assertThrows(OrderFlightException.class, () -> {
           orderService.getFlightFromOrderItems(flightItemMock);
        });
    }

    @Test
    void shouldReturnOnlyTaxItemFromOrder() {
        OrderItemEntity taxItemMock = MockBuilder.orderItemEntity();
        taxItemMock.setSkuId("tax");

        OrderItemEntity taxItem = orderService.getTaxFromOrderItems(Set.of(taxItemMock));

        assertInstanceOf(OrderItemEntity.class, taxItem);
        assertEquals(taxItemMock, taxItem);
    }
    @Test
    void shouldThrowTaxItemFromOrderWhenNotFound() {
        Set<OrderItemEntity> taxItemMock = Set.of();

        assertThrows(OrderFlightException.class, () -> {
           orderService.getTaxFromOrderItems(taxItemMock);
        });
    }

    @Test
    void shouldLogOrderDetail() {
        OrderEntity order = MockBuilder.orderEntity();
        OrderItemEntity taxItemMock = MockBuilder.orderItemEntity();
        taxItemMock.setSkuId("tax");
        order.getItems().add(taxItemMock);

        assertAll(() -> {
            orderService.orderDetailLog("test", StatusLivelo.PROCESSING.getCode(), order);
        });
    }

    @Test
    void shouldUpdateSubmittedDate() {
        OrderEntity order = MockBuilder.orderEntity();
        String date = "2013-09-29T18:46:19Z";
        
        assertDoesNotThrow(() -> {
            orderService.updateSubmittedDate(order, date);
        });
    }

    @Test
    void shouldUpdateSubmittedDateFailed() {
        OrderEntity order = MockBuilder.orderEntity();
        String date = "2013-09-29T18:46:19";
        
        assertDoesNotThrow(() -> {
            orderService.updateSubmittedDate(order, date);
        });
    }

    @Test
    void shouldValidOrder() {
        OrderEntity order = MockBuilder.orderEntity();
        Optional<OrderEntity> mockedOrder = Optional.of(order);
        OrderValidateRequestDTO orderValidateRequest = MockBuilder.orderValidateRequest();

        when(orderRepository.findByCommerceOrderIdInAndExpirationDateAfter(any(), any())).thenReturn(Optional.of(order));
        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("id"))).thenReturn(mockedOrder);

        var response = orderService.validateOrderList(orderValidateRequest);
        assertInstanceOf(OrderValidateResponseDTO.class, response);
    }
    @Test
    void shouldThrowErrorWhenTryToValidOrder() {
        OrderEntity order = null;
        OrderValidateRequestDTO orderValidateRequest = MockBuilder.orderValidateRequest();

        when(orderService.findByCommerceOrderIdInAndExpirationDateAfter(List.of("id"))).thenReturn(Optional.empty());

        assertThrows(OrderFlightException.class, () -> {
            orderService.validateOrderList(orderValidateRequest);
        });
    }
}
