package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.*;
import br.com.livelo.orderflight.domain.entity.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LiveloPartnersMapper {

//    @Mapping(source = "currentStatus", target = "items.status")
    @Mapping(target = "amount", source = "price.amount")
    @Mapping(target = "currency", constant = "PTS")
//    @Mapping(target = "items", expression = "java(testing(order))")
    UpdateOrderDTO orderEntityToUpdateOrderDTO(OrderEntity order);

    @Mapping(source = "partnerOrderLinkId", target = "partnerOrderId")
    @Mapping(source = "skuId", target = "id")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(target = "currency", constant = "PTS")
//    @Mapping(target = "partnerInfo.travel.legs", source = "segments.flightLegs")
    ItemDTO orderItemEntityToItemDTO(OrderItemEntity orderItemEntity);



//    PartnerInfoSummaryDTO TravelInfoEntityToPartnerInfoSummaryDTO(TravelInfoEntity travelInfo);

//
    @Mapping(source = "description", target = "message")
    @Mapping(source = "partnerDescription", target = "details")
    StatusDTO orderStatusEntityToStatusDTO(OrderStatusEntity orderStatusEntity);



    @Mapping(target = "managedAirline.name", source = "managedBy")
    @Mapping(target = "managedAirline.iata", source = "managedBy")
    @Mapping(target = "operationAirline.name", source = "airline")
    @Mapping(target = "operationAirline.iata", source = "airline")
    @Mapping(target = "departure.iata", source = "destinationIata")
    @Mapping(target = "arrival.iata", source = "originIata")
    LegSummaryDTO flightLegEntityToLegSummaryDTO(FlightLegEntity flightLegEntity);


//    default List<LegSummaryDTO> mapLegs(OrderEntity order) {
//        return order.getItems().stream().map(orderItemEntity -> orderItemEntity.getSegments().stream().map(segment -> segment.getFlightsLegs().stream().map(this::flightLegEntityToLegSummaryDTO).toList()))
//
//    }
//        return order.getItems().stream().map(orderItemEntity -> orderItemEntity.getSegments().stream().map(segment -> segment.getFlightsLegs().stream().map(this::flightLegEntityToLegSummaryDTO).toList()))



//        return orderEntity.getItems().stream()
//                .filter(item -> !item.getSkuId().toUpperCase().contains("TAX"))
//                .findFirst()
//                .map(item -> item.getTravelInfo().getPaxs().stream()
//                        .map(this::paxEntityToConnectorConfirmOrderPaxRequest)
//                        .toList())
//                .orElse(Collections.emptyList());
//    }
}

//
//    default Object testing(OrderEntity order) {
//        var currentStatus = orderStatusEntityToStatusDTO(order.getCurrentStatus());
//        return order.getItems().stream().map(orderItemEntity::set);
//    }
//
//    @AfterMapping
//    default void setItemStatus(UpdateOrderDTO dto, OrderStatusEntity status) {
//        StatusDTO statusDTO = orderStatusEntityToStatusDTO(status);
//        dto.getItems().forEach(itemDTO -> itemDTO.setStatus(statusDTO));
