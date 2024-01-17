package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.*;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {ReservationItemMapper.class, ReservationPriceMapper.class})
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

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
    @Mapping(target = "status", expression="java(mapStatus(partnerReservationResponse))")
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationResponse, listPrice))")
    OrderEntity toOrderEntity(ReservationRequest cartRequest, PartnerReservationResponse partnerReservationResponse, String transactionId, String customerId, String channel, String listPrice);

    default OrderPriceEntity mapPrice(PartnerReservationResponse partnerReservationResponse, String listPrice) {
        ReservationPriceMapper reservationPriceMapper = Mappers.getMapper(ReservationPriceMapper.class);
        return reservationPriceMapper.toOrderPriceEntity(partnerReservationResponse, listPrice);
    }

    default OrderStatusEntity mapStatus(PartnerReservationResponse partnerReservationResponse) {
        ReservationStatusMapper reservationStatusMapper = Mappers.getMapper(ReservationStatusMapper.class);
        return reservationStatusMapper.toOrderStatus(partnerReservationResponse);
    }

    default Set<OrderItemEntity> mapItems(ReservationRequest cartRequest, PartnerReservationResponse partnerReservationResponse) {
        ReservationItemMapper reservationItemMapper = Mappers.getMapper(ReservationItemMapper.class);

        return cartRequest.getItems()
                .stream()
                .map(currentRequestItem -> reservationItemMapper.toOrderItemEntity(currentRequestItem,
                        partnerReservationResponse.getItems().stream().filter(currentPartnerReservation -> currentPartnerReservation.getType().equals(currentRequestItem.getProductType())).toList().getFirst())
                )
                .collect(Collectors.toSet());
    }

    PartnerReservationRequest toPartnerReservationRequest(ReservationRequest request);

    PartnerReservationDocument toPartnerReservationDocument(ReservationDocument reservationDocument);

    ReservationResponse toReservationResponse(OrderEntity orderEntity);
    
}