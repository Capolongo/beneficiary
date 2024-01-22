package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationDocument;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationDocument;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {ReservationItemMapper.class, ReservationPriceMapper.class})
public interface ReservationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", expression = "java(mapItems(reservationRequest, partnerReservationResponse, listPrice))")
    @Mapping(target = "expirationDate", source = "partnerReservationResponse.expirationDate")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "commerceOrderId", source = "reservationRequest.commerceOrderId")
    @Mapping(target = "partnerOrderId", source = "partnerReservationResponse.partnerOrderId")
    @Mapping(target = "partnerCode", source = "partnerReservationResponse.partnerCode")
    @Mapping(target = "channel", source = "channel")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "customerIdentifier", source = "customerId")
    @Mapping(target = "statusHistory",  expression = "java(Set.of(mapStatus(partnerReservationResponse)))")
    @Mapping(target = "status", expression = "java(mapStatus(partnerReservationResponse))")
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationResponse, listPrice))")
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
                .map(currentRequestItem ->
                        reservationItemMapper.toOrderItemEntity(
                                currentRequestItem,
                                partnerReservationResponse.getItems().stream()
                                        .filter(currentPartnerReservation ->
                                                currentPartnerReservation.getType().equals(currentRequestItem.getProductType())
                                        ).toList().getFirst(), listPrice
                        )
                )
                .collect(Collectors.toSet());
    }

    PartnerReservationRequest toPartnerReservationRequest(ReservationRequest request);

    PartnerReservationDocument toPartnerReservationDocument(ReservationDocument reservationDocument);

    ReservationResponse toReservationResponse(OrderEntity orderEntity);

}