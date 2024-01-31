package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {ReservationItemPriceMapper.class, ReservationTravelInfoEntityMapper.class, ReservationSegmentsMapper.class})
public interface ReservationItemMapper {
    @Mapping(target = "commerceItemId", source = "reservationItem.commerceItemId")
    @Mapping(target = "productId", source = "reservationItem.productId")
    @Mapping(target = "quantity", source = "partnerReservationItem.quantity")
    @Mapping(target = "price", expression = "java(mapPrice(partnerReservationItem, listPrice))")
    @Mapping(target = "travelInfo", expression = "java(mapTravelInfo(reservationRequest, partnerReservationItem))")
    @Mapping(target = "segments", expression = "java(mapSegments(partnerReservationItem))")
    OrderItemEntity toOrderItemEntity(ReservationRequest reservationRequest, ReservationItem reservationItem, PartnerReservationItem partnerReservationItem, String listPrice);

    default OrderItemPriceEntity mapPrice(PartnerReservationItem partnerReservationItem, String listPrice) {
        var reservationItemPriceMapper = Mappers.getMapper(ReservationItemPriceMapper.class);
        return reservationItemPriceMapper.toOrderItemPriceEntity(partnerReservationItem, listPrice);
    }

    default TravelInfoEntity mapTravelInfo(ReservationRequest reservationRequest, PartnerReservationItem partnerReservationItem) {
        var mapper = Mappers.getMapper(ReservationTravelInfoEntityMapper.class);

        return mapper.toReservationTravelInfoEntity(reservationRequest, partnerReservationItem.getTravelInfo());
    }

    default Set<SegmentEntity> mapSegments(PartnerReservationItem partnerReservationItem) {
        var mapper = Mappers.getMapper(ReservationSegmentsMapper.class);

        if (partnerReservationItem != null) {
            List<PartnerReservationSegment> segments = partnerReservationItem.getSegments();

            if (segments != null) {
                return partnerReservationItem.getSegments()
                        .stream()
                        .map(mapper::toSegmentEntity)
                        .collect(Collectors.toSet());
            }


        }

        return Collections.emptySet();
    }

}