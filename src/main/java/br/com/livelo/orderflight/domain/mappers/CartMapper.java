package br.com.livelo.orderflight.domain.mappers;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {CartItemMapper.class, CartPriceMapper.class})
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submittedDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "items", expression = "java(mapItems(cartRequest, partnerReservationResponse))")
    @Mapping(target = "expirationDate", source = "partnerReservationResponse.expirationDate")
    @Mapping(target = "commerceOrderId", source = "cartRequest.commerceOrderId")
    @Mapping(target = "partnerOrderId", source = "partnerReservationResponse.partnerOrderId")
    @Mapping(target = "partnerCode", source = "partnerReservationResponse.partnerCode")
    @Mapping(target = "channel", source = "channel")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "customerIdentifier", source = "customerId")
    //    TODO entender o status e o statusHistory
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationResponse))")
    OrderEntity toOrderEntity(CartRequest cartRequest, PartnerReservationResponse partnerReservationResponse, String transactionId, String customerId, String channel);


    default OrderPriceEntity mapPrice(PartnerReservationResponse partnerReservationResponse) {
        CartPriceMapper cartPriceMapper = Mappers.getMapper(CartPriceMapper.class);

        return cartPriceMapper.toOrderPriceEntity(partnerReservationResponse);
    }

    default Set<OrderItemEntity> mapItems(CartRequest cartRequest, PartnerReservationResponse partnerReservationResponse) {
        CartItemMapper cartItemMapper = Mappers.getMapper(CartItemMapper.class);

        return cartRequest.getItems()
                .stream()
                .map(currentRequestItem -> cartItemMapper.toOrderItemEntity(currentRequestItem,
                        partnerReservationResponse.getItems().stream().filter(currentPartnerReservation -> currentPartnerReservation.getType().equals(currentRequestItem.getProductType())).toList().getFirst())
                )
                .collect(Collectors.toSet());
        }
    }









