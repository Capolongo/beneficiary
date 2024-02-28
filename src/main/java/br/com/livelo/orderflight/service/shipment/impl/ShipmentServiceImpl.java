package br.com.livelo.orderflight.service.shipment.impl;

import br.com.livelo.orderflight.constants.ShipmentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.shipment.CommerceItem;
import br.com.livelo.orderflight.domain.dtos.shipment.CommerceItemDTO;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentsDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.shipment.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final OrderService orderService;

    private final ShipmentOptionConstant shipmentOptionConstant;

    @Override
    public ShipmentOptionsResponse getShipmentOptions(String id, String postalCode) {
        log.debug("ShipmentServiceImpl.getShipmentOptions - start. tempPartnerOrderId: [{}], postalCode: [{}]", id, postalCode);
        final OrderEntity orderEntity = orderService.getOrderById(id);
        ShipmentOptionsResponse shipmentOptionsResponse = buildShipmentOptionsResponse(id, orderEntity);
        log.debug("ShipmentServiceImpl.getShipmentOptions - end shipmentOptionsResponse: [{}]", shipmentOptionsResponse);
        return shipmentOptionsResponse;
    }

    private  ShipmentOptionsResponse buildShipmentOptionsResponse(String partnerOrderId, OrderEntity order) {
        return ShipmentOptionsResponse.builder()
                .shipmentOptions(buildShipmentOptions(partnerOrderId, order))
                .build();
    }

    private List<ShipmentsDTO> buildShipmentOptions(String partnerOrderId, OrderEntity order) {
        log.debug("ShipmentServiceImpl.buildShipmentOptions - start. [shipmentConstants]: {}", shipmentOptionConstant);
        return Collections.singletonList(ShipmentsDTO.builder()
                .id(shipmentOptionConstant.getId())
                .currency(shipmentOptionConstant.getCurrency())
                .price(shipmentOptionConstant.getPrice())
                .type(shipmentOptionConstant.getType())
                .items(skusIds(order))
                .description(shipmentOptionConstant.getDescription())
                .commerceItems(buildCommerce(partnerOrderId, order))
                .build());
    }

    private static List<CommerceItemDTO> buildCommerce(final String partnerOrderId, final OrderEntity order) {
        return order.getItems().stream()
                .map(item -> CommerceItemDTO.builder()
                        .deliveryDate(Date.from(order.getLastModifiedDate().toInstant()))
                        .commerceItems(buildCommerceItems(partnerOrderId, item))
                        .build()
                ).collect(Collectors.toList());
    }

    private static List<CommerceItem> buildCommerceItems(final String partnerOrderId, final OrderItemEntity item) {
        return Collections.singletonList(CommerceItem.builder()
                .id(item.getSkuId())
                .partnerOrderId(partnerOrderId)
                .commerceItemId(item.getCommerceItemId())
                .partnerOrderLinkId(item.getPartnerOrderLinkId())
                .build());
    }

    private List<String> skusIds(final OrderEntity order) {
        return order.getItems().stream()
                .map(OrderItemEntity::getSkuId
                ).collect(Collectors.toList());
    }

}
