package br.com.livelo.orderflight.service.status;


import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusItemDTO;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.enuns.StatusLivelo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.mappers.ConfirmOrderMapper;
import br.com.livelo.orderflight.mappers.StatusMapper;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.status.impl.StatusServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceImplTest {

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private  StatusMapper statusMapper;

    @Mock
    private ConfirmOrderMapper confirmOrderMapper;

    @InjectMocks
    private StatusServiceImpl statusService;



    @Test
    void shouldSucessUpdateStatus() {

        String id = "lf20";

        OrderStatusEntity orderStatus =  mockOrderStatus();

        OrderEntity mockedOrder = MockBuilder.orderEntity();
        mockedOrder.setCurrentStatus(MockBuilder.statusFaill());
        UpdateStatusDTO updateStatus = mockBuildUpdateStatusDTO(StatusLivelo.PROCESSING.getCode(), StatusLivelo.PROCESSING.getDescription(), StatusLivelo.PROCESSING.getDescription());

        UpdateStatusItemDTO items = mockBuildStatusItemDTO(updateStatus, "commerceItemId");

        UpdateStatusRequest request = mockCommerceOrderId(items, "commerceOrderId");

        when(orderService.getOrderById(anyString())).thenReturn(mockedOrder);

        when(statusMapper.convert(any(UpdateStatusDTO.class))).thenReturn(orderStatus);

        doNothing().when(orderService).addNewOrderStatus(any(OrderEntity.class), any(OrderStatusEntity.class));

        when(orderService.save(any(OrderEntity.class))).thenReturn(mock(OrderEntity.class));

        when(confirmOrderMapper.orderEntityToConfirmOrderResponse(any()))
                .thenReturn(MockBuilder.confirmOrderResponse());

        var response  = statusService.updateStatus(id, request);

        assertEquals(MockBuilder.confirmOrderResponse(), response);
    }

    @Test
    void shouldErrorValidCommerceOrderIdEqualOrderId() {

        String id = "lf20";

        OrderEntity mockedOrder = MockBuilder.orderEntity();

        UpdateStatusDTO updateStatus = mockBuildUpdateStatusDTO(StatusLivelo.CANCELED.getCode(), StatusLivelo.CANCELED.getDescription(), StatusLivelo.CANCELED.getDescription());

        UpdateStatusItemDTO items = mockBuildStatusItemDTO(updateStatus, "commerceItemId");

        UpdateStatusRequest request = mockCommerceOrderId(items, "1");

        when(orderService.getOrderById(anyString())).thenReturn(mockedOrder);

        assertThrows(OrderFlightException.class, () -> {
            statusService.updateStatus(id, request);
        });
    }

    @Test
    void shouldErrorvalidCommerceOrderIdEqualOrderId() {

        String id = "lf20";

        OrderEntity mockedOrder = MockBuilder.orderEntity();

        UpdateStatusDTO updateStatus = mockBuildUpdateStatusDTO(StatusLivelo.CANCELED.getCode(), StatusLivelo.CANCELED.getDescription(), StatusLivelo.CANCELED.getDescription());

        UpdateStatusItemDTO items = mockBuildStatusItemDTO(updateStatus, "1");

        UpdateStatusRequest request = mockCommerceOrderId(items, "commerceOrderId");

        when(orderService.getOrderById(anyString())).thenReturn(mockedOrder);

        assertThrows(OrderFlightException.class, () ->  {
            statusService.updateStatus(id, request);
        });
    }

    @Test
    void shouldErrorValidStatusInitialIsProcess() {

        String id = "lf20";

        OrderEntity mockedOrder = MockBuilder.orderEntity();

        UpdateStatusDTO updateStatus = mockBuildUpdateStatusDTO(StatusLivelo.PROCESSING.getCode(), StatusLivelo.PROCESSING.getDescription(), StatusLivelo.PROCESSING.getDescription());

        UpdateStatusItemDTO items = mockBuildStatusItemDTO(updateStatus, "commerceItemId");

        UpdateStatusRequest request = mockCommerceOrderId(items, "commerceOrderId");

        when(orderService.getOrderById(anyString())).thenReturn(mockedOrder);

        assertThrows(OrderFlightException.class, () ->  {
            statusService.updateStatus(id, request);
        });
    }

    private OrderStatusEntity mockOrderStatus() {
        return OrderStatusEntity.builder()
                .id(1L)
                .partnerCode("")
                .partnerDescription("")
                .partnerResponse("")
                .statusDate(LocalDateTime.now())
                .code("LIVPNR-9001")
                .description("CANCELED")
                .build();
    }

    private static UpdateStatusRequest mockCommerceOrderId(UpdateStatusItemDTO items, String orderId) {
        return UpdateStatusRequest.builder()
                .orderId(orderId)
                .items(List.of(items))
                .build();
    }

    private static UpdateStatusItemDTO mockBuildStatusItemDTO(UpdateStatusDTO updateStatus, String commerceItemId) {
        return UpdateStatusItemDTO.builder()
                .commerceItemId(commerceItemId)
                .id("CVCFLIGHTTAX")
                .user("Livelo")
                .reason("123")
                .status(updateStatus)
                .build();
    }

    private static UpdateStatusDTO mockBuildUpdateStatusDTO(String code, String message, String details) {
        return UpdateStatusDTO
                .builder()
                .code(code)
                .message(message)
                .details(details)
                .build();
    }
}
