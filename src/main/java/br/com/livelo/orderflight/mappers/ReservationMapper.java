package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.*;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import br.com.livelo.orderflight.exception.OrderFlightException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR;

@Mapper(componentModel = "spring", uses = {ReservationItemMapper.class, ReservationPriceMapper.class})
public interface ReservationMapper {
    @Mapping(target = "items", expression = "java(mapItems(reservationRequest, partnerReservationResponse, listPrice))")
    @Mapping(target = "expirationDate", source = "partnerReservationResponse.expirationDate")
    @Mapping(target = "commerceOrderId", source = "reservationRequest.commerceOrderId")
    @Mapping(target = "partnerOrderId", source = "partnerReservationResponse.partnerOrderId")
    @Mapping(target = "partnerCode", source = "reservationRequest.partnerCode")
    @Mapping(target = "channel", source = "channel")
    @Mapping(target = "tierCode", ignore = true)
    @Mapping(target = "originOrder", ignore = true)
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "customerIdentifier", source = "customerId")
    @Mapping(target = "statusHistory", expression = "java(Set.of(mapStatus(partnerReservationResponse)))")
    @Mapping(target = "currentStatus", expression = "java(mapStatus(partnerReservationResponse))")
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationResponse, listPrice))")
    @Mapping(target = "createDate", ignore = true)
    OrderEntity toOrderEntity(ReservationRequest reservationRequest, PartnerReservationResponse partnerReservationResponse, String transactionId, String customerId, String channel, String listPrice);

    default OrderPriceEntity mapPrice(PartnerReservationResponse partnerReservationResponse, String listPrice) {
        var reservationPriceMapper = Mappers.getMapper(ReservationPriceMapper.class);
        return reservationPriceMapper.toOrderPriceEntity(partnerReservationResponse, listPrice);
    }

    default OrderStatusEntity mapStatus(PartnerReservationResponse partnerReservationResponse) {
        var reservationStatusMapper = Mappers.getMapper(ReservationStatusMapper.class);
        return reservationStatusMapper.toOrderStatus(partnerReservationResponse);
    }

    default Set<OrderItemEntity> mapItems(ReservationRequest reservationRequest, PartnerReservationResponse partnerReservationResponse, String listPrice) {
        var reservationItemMapper = Mappers.getMapper(ReservationItemMapper.class);

        return reservationRequest.getItems()
                .stream()
                .map(currentRequestItem -> reservationItemMapper.toOrderItemEntity(
                        reservationRequest,
                        currentRequestItem,
                        partnerReservationResponse.getItems().stream()
                                .filter(currentPartnerReservationResponseItem -> currentPartnerReservationResponseItem
                                        .getType()
                                        .equals(currentRequestItem
                                                .getProductType()))
                                .toList().getFirst(),
                        listPrice
                ))
                .collect(Collectors.toSet());
    }

    @Mapping(target = "segmentsPartnerIds", source = "segmentsPartnerIds", qualifiedByName = "mapSegmentsPartnerIds")
    PartnerReservationRequest toPartnerReservationRequest(ReservationRequest request);

    @Named("mapSegmentsPartnerIds")
    default List<String> mapSegmentsPartnerdIds(List<String> segmentsPartnersIds) {
        var segmentId =  segmentsPartnersIds.stream()
                .filter(segment -> !segment.equals("."))
                .findFirst()
                .orElseThrow(() ->new OrderFlightException(ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR, null, "There's no segment token valid sent for the client!"));
        return List.of(segmentId);
    }

    @Mapping(target = "number", source = "documentNumber")
    PartnerReservationDocument toPartnerReservationDocument(ReservationDocument reservationDocument);

    @Mapping(target = "expirationTimer", source = "expirationTimer")
    @Mapping(target = "orderId", source = "orderEntity.id")
    ReservationResponse toReservationResponse(OrderEntity orderEntity, int expirationTimer);
}
