package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.*;
import br.com.livelo.orderflight.domain.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LiveloPartnersMapper {

    @Mapping(target = "amount", source = "price.amount")
    @Mapping(target = "currency", constant = "PTS")
    @Mapping(target = "items", expression = "java(buildItemsDTO(order))")
    UpdateOrderDTO orderEntityToUpdateOrderDTO(OrderEntity order);

    default boolean isTaxItem(String skuId) {
        return skuId.toUpperCase().contains("TAX");
    }

    default List<ItemDTO> buildItemsDTO(OrderEntity order) {
        StatusDTO statusDTO = orderStatusEntityToStatusDTO(order.getCurrentStatus());

        var flight = order.getItems().stream().filter(item -> !isTaxItem(item.getSkuId())).toList();

        var travelSummary = TravelSummaryDTO.builder()
                .tour(null)
//                .flights(buildFlights(flight.get(0).getSegments()))
                .flights(buildFlights(flight.get(0).getSegments(), flight.get(0).getTravelInfo()))
                .build();
        var partnerInfo = PartnerInfoSummaryDTO.builder().travel(travelSummary).build();

        return order.getItems().stream().map(item -> {
            var mappedItem = orderItemEntityToItemDTO(item);
            mappedItem.setStatus(statusDTO);

            if (!isTaxItem(item.getSkuId())) {
                mappedItem.setPartnerInfo(partnerInfo);
            }

            return mappedItem;
        }).toList();

    }

    default List<FlightSummaryDTO> buildFlights(Set<SegmentEntity> segments, TravelInfoEntity travelInfo) {
        var gds = GlobalDistribuitionSystemDTO.builder().reservationCode(travelInfo.getReservationCode()).build();

        List<CustomerDTO> mappedPaxs = travelInfo.getPaxs().stream().map(this::paxEntityToCustomerDTO).toList();
        return segments.stream().map(segment -> {
            var flight = segmentEntityToFlightSummaryDTO(segment);
            flight.setPassengers(mappedPaxs);
            flight.setGlobalDistribuitionSystem(gds);
            return flight;
        }).toList();
    }

    @Mapping(target = "partnerOrderId", source = "partnerOrderLinkId")
    @Mapping(target = "id", source = "skuId")
    @Mapping(target = "price", source = "price.amount")
    @Mapping(target = "currency", constant = "PTS")
    ItemDTO orderItemEntityToItemDTO(OrderItemEntity orderItemEntity);

    @Mapping(source = "description", target = "message")
    @Mapping(source = "partnerDescription", target = "details")
    StatusDTO orderStatusEntityToStatusDTO(OrderStatusEntity orderStatusEntity);

    @Mapping(target = "managedAirline.name", source = "managedBy")
    @Mapping(target = "managedAirline.iata", source = "managedBy")
    @Mapping(target = "operationAirline.name", source = "airline")
    @Mapping(target = "operationAirline.iata", source = "airline")
    @Mapping(target = "departure.date", source = "departureDate")
    @Mapping(target = "departure.iata", source = "originIata")
    @Mapping(target = "arrival.iata", source = "destinationIata")
    @Mapping(target = "arrival.date", source = "arrivalDate")
    @Mapping(target = "duration", source = "flightDuration")
    @Mapping(target = "departureName", source = "originDescription")
    @Mapping(target = "arrivalName", source = "destinationDescription")
    LegSummaryDTO flightLegEntityToLegSummaryDTO(FlightLegEntity flightLegEntity);

    @Mapping(target = "documents", source = "document")
    CustomerDTO paxEntityToCustomerDTO(PaxEntity paxEntity);

    @Mapping(target = "doc", source = "documentNumber")
    @Mapping(target = "issuingDate", source = "issueDate")
    DocumentDTO documentEntityToDocumentDTO(DocumentEntity documentEntity);


    List<ServiceDTO> segmentEntityToServiceDTO(Set<SegmentEntity> segmentEntity);


//    finalizar esse mapper
    @Mapping(target = "services", expression = "java(mapServices(segmentEntity.getCancelationRules(), segmentEntity.getLuggages(), segmentEntity.getChangeRules()))")
    @Mapping(target = "duration", source = "flightDuration")
    @Mapping(target = "legs", source = "flightsLegs")
    @Mapping(target = "baggage", expression = "java(setBaggage(segmentEntity.getLuggages()))")
//    @Mapping(target = "arrival")
    FlightSummaryDTO segmentEntityToFlightSummaryDTO(SegmentEntity segmentEntity); // PRecisamos saber se é TravelSummaryDTO ou FlightSummaryDTO

    @Mapping(target = "isIncluded", constant = "false")
        // isIncluded não existe no Object do parâmetro, por default estou colocando false mas deve ser validado quando deve ser true.
    ServiceDTO cancellationRuleEntityToServiceDTO(CancelationRuleEntity cancelationRuleEntity);

    @Mapping(target = "isIncluded", constant = "false")
        // isIncluded não existe no Object do parâmetro, por default estou colocando false mas deve ser validado quando deve ser true.
    ServiceDTO luggageEntityEntityToServiceDTO(LuggageEntity luggageEntity);

    @Mapping(target = "isIncluded", constant = "false")
        // isIncluded não existe no Object do parâmetro, por default estou colocando false mas deve ser validado quando deve ser true.
    ServiceDTO changeRuleEntityToServiceDTO(ChangeRuleEntity changeRuleEntity);

    @Mapping(target = "date", source = "departureDate")
    @Mapping(target = "airportName", source = "originIata")
    @Mapping(target = "iata", source = "originIata")
//    @Mapping(target = "numberOfStops", source = "stops")
    DepartureDTO flightLegEntityToDepartureDTO(FlightLegEntity flightLegEntity);

    @Mapping(target = "date", source = "arrivalDate")
    @Mapping(target = "airportName", source = "destinationIata")
    @Mapping(target = "iata", source = "destinationIata")
//    @Mapping(target = "numberOfStops", source = "stops")
    ArrivalDTO flightLegEntityToArrivalDTO(FlightLegEntity flightLegEntity);

    BaggageDTO luggageToBaggageDTO(LuggageEntity luggageEntity);

    default BaggageDTO setBaggage(Set<LuggageEntity> luggages) {
        var bagLuggage = luggages.stream().filter(luggage -> "BAG".equals(luggage.getType())).toList().stream().findFirst();

        if (bagLuggage.isPresent()) {
            return luggageToBaggageDTO(bagLuggage.get());
        }

        var firstLuggage = luggages.stream().findFirst();
        return firstLuggage.map(this::luggageToBaggageDTO).orElse(null);
    }

    default ArrayList<ServiceDTO> mapServices(Set<CancelationRuleEntity> cancellationRules, Set<LuggageEntity> luggages, Set<ChangeRuleEntity> changeRules) {
        ArrayList<ServiceDTO> services = new ArrayList<ServiceDTO>();
        cancellationRules.forEach(rule -> services.add(cancellationRuleEntityToServiceDTO(rule)));
        luggages.forEach(luggage -> services.add(luggageEntityEntityToServiceDTO(luggage)));
        changeRules.forEach(rule -> services.add(changeRuleEntityToServiceDTO(rule)));
        return services;
    }

    // DestinationDTO segmentEntityToDestinationDTO(SegmentEntity segmentEntity);
}
