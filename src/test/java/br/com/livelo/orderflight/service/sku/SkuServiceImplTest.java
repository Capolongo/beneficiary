package br.com.livelo.orderflight.service.sku;

import br.com.livelo.orderflight.constants.SkuConstant;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import br.com.livelo.orderflight.service.order.impl.OrderServiceImpl;
import br.com.livelo.orderflight.service.sku.impl.SkuServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource({
            "BRL, 1, true, 1000, 100, 100",
            "PTS, 1, true, 1000, 100, 1000",
            "BRL, 1, false, 1000, 100, 2",
            "PTS, 1, false, 1000, 100, 2",
            ", 1, true, 1000, 100, 2",
            "PTS, 1, true, , 100, 2",
            "PTS, 1, true, , , 2",
            "PTS, , true, , , 2",
            ", , true, , , 2",
            "BRL, 1, true, 1000, , 2",
    })
    void shouldSuccessGetSku(String currency, String commerceItemId, boolean hasPrice, BigDecimal pointsAmount, BigDecimal amount, BigDecimal expectedSalePrice) {
        when(skuConstant.getCurrency()).thenReturn(currency);
        when(skuConstant.getListPrice()).thenReturn(new BigDecimal(2));
        when(skuConstant.getSalePrice()).thenReturn(new BigDecimal(2));
        when(skuConstant.getQuantity()).thenReturn(1);
        when(skuConstant.getDescription()).thenReturn("DEFAULT");
        when(skuConstant.getAvailable()).thenReturn(true);

        if (currency != null && commerceItemId != null) {
            when(orderService.findByCommerceItemIdAndSkuId(any(), any())).thenReturn(buildOrderItemEntity(hasPrice, pointsAmount, amount));
        }

        SkuItemResponse response = skuService.getSku("SKU", commerceItemId, currency);

        assertEquals("SKU", response.getSkuId());
        assertEquals("DEFAULT SKU SKU", response.getDescription());
        assertEquals(currency, response.getCurrency());
        assertTrue(response.isAvailable());
        assertEquals(response.getSalePrice(), expectedSalePrice);
        assertEquals(response.getListPrice(), expectedSalePrice);
        assertEquals(1, response.getQuantity());
    }

    private OrderItemEntity buildOrderItemEntity(boolean hasPrice, BigDecimal pointsAmount, BigDecimal amount) {
        return OrderItemEntity.builder()
                .price(hasPrice
                        ? OrderItemPriceEntity.builder()
                        .pointsAmount(pointsAmount)
                        .amount(amount)
                        .build()
                        : null)
                .build();
    }
}