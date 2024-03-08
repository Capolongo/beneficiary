package br.com.livelo.orderflight.service.shipment;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.constants.ShipmentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.installment.impl.InstallmentServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.shipment.impl.ShipmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private ShipmentOptionConstant shipmentOptionConstant;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Test
    void shouldSucessGetShipmentOptions() throws Exception {

        when(orderService.getOrderById(eq("lt1"))).thenReturn(MockBuilder.orderEntity());

        when(shipmentOptionConstant.getCurrency()).thenReturn("PTS");
        when(shipmentOptionConstant.getId()).thenReturn(1L);
        when(shipmentOptionConstant.getDescription()).thenReturn("Sem entrega física");
        when(shipmentOptionConstant.getPrice()).thenReturn(new BigDecimal(1));
        when(shipmentOptionConstant.getType()).thenReturn("Electronica");


        ShipmentOptionsResponse response = shipmentService.getShipmentOptions("lt1", "1");

        assertNotNull(response);


    }

}