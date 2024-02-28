package br.com.livelo.orderflight.service.sku.impl;

import br.com.livelo.orderflight.constants.SkuConstant;
import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import br.com.livelo.orderflight.repository.ItemRepository;
import br.com.livelo.orderflight.service.sku.SkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final ItemRepository itemRepository;

    private final SkuConstant skuConstant;

    @Override
    public SkuItemResponse getSku(final String skuId, final String commerceItemId, final String currency){
        log.debug("SkuServiceImpl.getSku - start. [skuId]: {}, [commerceItemId]: {}, [currency]: {}", skuId, commerceItemId, currency);

        return getSkuResponse(skuId, commerceItemId, currency);
    }

    private SkuItemResponse getSkuResponse(String skuId, String commerceItemId, String currency){
        SkuItemResponse skuItemResponseDTOBase = buildSku(skuId);

        if(currencyAndCommerceItemIdIsPresent(currency, commerceItemId)){
            skuItemResponseDTOBase = enrichSkuWithCommerceItem(commerceItemId, skuItemResponseDTOBase);
            return skuItemResponseDTOBase;
        }

        return skuItemResponseDTOBase;

    }

    private Boolean currencyAndCommerceItemIdIsPresent(String currency, String commerceItemId){
        return StringUtils.hasText(commerceItemId) && StringUtils.hasText(currency);
    }

    private SkuItemResponse buildSku(String skuId){
        log.debug("SkuServiceImpl.buildSku - start [skuId]: {}, [skuConstants]: {}", skuId, skuConstant);

        return SkuItemResponse
                .builder()
                .skuId(skuId)
                .available(skuConstant.getAvailable())
                .salePrice(skuConstant.getSalePrice())
                .listPrice(skuConstant.getListPrice())
                .currency(skuConstant.getCurrency())
                .quantity(skuConstant.getQuantity())
                .description(skuConstant.getDescription() + " " + skuId + " SKU")
                .build();
    }

    private SkuItemResponse enrichSkuWithCommerceItem(String commerceItemId, SkuItemResponse skuItemResponseDTO){
        log.debug("SkuServiceImpl.enrichSkuWithCommerceItem - start commerceItemId and currency present start sku enrich process for [item]: {}", commerceItemId);
        final Optional<OrderItemEntity> itemOptional = itemRepository.findByCommerceItemIdAndSkuId(commerceItemId, skuItemResponseDTO.getSkuId());

        if (itemOptional.isEmpty()) {
            OrderFlightErrorType errorType = OrderFlightErrorType.VALIDATION_COMMERCE_ITEM_ID_OR_ID_SKU_NOT_FOUND;
            throw new OrderFlightException(errorType, errorType.getTitle(), null);
        }

        double listPrice = itemOptional.get().getPrice() != null && itemOptional.get().getPrice().getListPrice() != null ? Double.parseDouble(itemOptional.get().getPrice().getListPrice()) : skuConstant.getSalePrice();

        return skuItemResponseDTO
                .toBuilder()
                .commerceItemId(commerceItemId)
                .listPrice(listPrice)
                .salePrice(listPrice)
                .build();

    }

}
