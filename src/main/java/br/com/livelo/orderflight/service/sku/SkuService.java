package br.com.livelo.orderflight.service.sku;

import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;

public interface SkuService {
    SkuItemResponse getSku(String id, String commerceItemId, String currency);
}
