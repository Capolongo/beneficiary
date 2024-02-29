package br.com.livelo.orderflight.service.sku;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.constants.SkuConstant;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.mock.MockBuilder;
import br.com.livelo.orderflight.service.installment.impl.InstallmentServiceImpl;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.sku.impl.SkuServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkuServiceImplTest {
    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private SkuConstant skuConstant;

    @InjectMocks
    private SkuServiceImpl skuService;

    @Test
    void shouldSucessGetSkuByCommerceIdAndCurrency() throws Exception {

        when(orderService.findByCommerceItemIdAndSkuId(anyString(), any())).thenReturn(MockBuilder.orderItemEntity());

        when(skuConstant.getCurrency()).thenReturn("PTS");
        when(skuConstant.getListPrice()).thenReturn(new BigDecimal(1));
        when(skuConstant.getSalePrice()).thenReturn(new BigDecimal(1));
        when(skuConstant.getQuantity()).thenReturn(1);
        when(skuConstant.getDescription()).thenReturn("DEFAULT");
        when(skuConstant.getAvailable()).thenReturn(true);

        SkuItemResponse response = skuService.getSku("SKU", "1", "PTS");

        assertEquals(response.getSkuId(), "SKU");
        assertEquals(response.getDescription(), "DEFAULT SKU SKU");
        assertEquals(response.getCurrency(), "PTS");
        assertTrue(response.isAvailable());
        assertEquals(response.getSalePrice(), new BigDecimal(1000));
        assertEquals(response.getListPrice(), new BigDecimal(1000));
        assertEquals(response.getQuantity(), 1);
        assertEquals(response.getCommerceItemId(), "1");
    }

    @Test
    void shouldSucessGetSkuNotByCommerceIdAndCurrency() throws Exception {

        when(skuConstant.getCurrency()).thenReturn("PTS");
        when(skuConstant.getListPrice()).thenReturn(new BigDecimal(1));
        when(skuConstant.getSalePrice()).thenReturn(new BigDecimal(1));
        when(skuConstant.getQuantity()).thenReturn(1);
        when(skuConstant.getDescription()).thenReturn("DEFAULT");
        when(skuConstant.getAvailable()).thenReturn(true);

        SkuItemResponse response = skuService.getSku("SKU", null, null);

        assertEquals(response.getSkuId(), "SKU");
        assertEquals(response.getDescription(), "DEFAULT SKU SKU");
        assertEquals(response.getCurrency(), "PTS");
        assertTrue(response.isAvailable());
        assertEquals(response.getSalePrice(), new BigDecimal(1));
        assertEquals(response.getListPrice(), new BigDecimal(1));
        assertEquals(response.getQuantity(), 1);

    }

}