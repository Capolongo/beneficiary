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


    @Mapping(target = "partnerOrderId", source = "partnerOrderLinkId")
    @Mapping(target = "id", source = "skuId")
    @Mapping(target = "price", source = "price.amount")
    @Mapping(target = "currency", constant = "PTS")
    @Mapping(target = "deliveryDate", constant = "deliveryDate")
    @Mapping(target = "forceUpdate", constant = "forceUpdate")
    ItemDTO orderItemEntityToItemDTO(OrderItemEntity orderItemEntity);

    @Mapping(source = "description", target = "message")
    @Mapping(source = "partnerDescription", target = "details")
    StatusDTO orderStatusEntityToStatusDTO(OrderStatusEntity orderStatusEntity);

    @Mapping(target = "managedAirline.name", source = "airlineManagedByDescription")
    @Mapping(target = "managedAirline.iata", source = "airlineManagedByIata")
    @Mapping(target = "operationAirline.name", source = "airlineOperatedByDescription")
    @Mapping(target = "operationAirline.iata", source = "airlineOperatedByIata")
    @Mapping(target = "departure.date", source = "departureDate")
    @Mapping(target = "departure.iata", source = "originIata")
    @Mapping(target = "arrival.iata", source = "destinationIata")
    @Mapping(target = "arrival.date", source = "arrivalDate")
    @Mapping(target = "duration", source = "flightDuration")
    @Mapping(target = "departureName", source = "originDescription")
    @Mapping(target = "arrivalName", source = "destinationDescription")
    @Mapping(target = "departure.airportName", constant = "departure.airportName")
    @Mapping(target = "departure.cityName", constant = "departure.cityName")
    @Mapping(target = "arrival.airportName", constant = "arrival.airportName")
    @Mapping(target = "arrival.cityName", constant = "arrival.cityName")
    LegSummaryDTO flightLegEntityToLegSummaryDTO(FlightLegEntity flightLegEntity);

    @Mapping(target = "phones", expression = "java(setPhone(paxEntity))")
    @Mapping(target = "notes", constant = "notes")
    CustomerDTO paxEntityToCustomerDTO(PaxEntity paxEntity);

    @Mapping(target = "doc", source = "documentNumber")
    @Mapping(target = "issuingDate", source = "issueDate")
    DocumentDTO documentEntityToDocumentDTO(DocumentEntity documentEntity);

    List<ServiceDTO> segmentEntityToServiceDTO(Set<SegmentEntity> segmentEntity);

    @Mapping(target = "services", expression = "java(mapServices(segmentEntity.getCancelationRules(), segmentEntity.getLuggages(), segmentEntity.getChangeRules()))")
    @Mapping(target = "duration", source = "flightDuration")
    @Mapping(target = "legs", source = "flightsLegs")
    @Mapping(target = "baggage", expression = "java(setBaggage(segmentEntity.getLuggages()))")
    @Mapping(target = "departure.date", source = "departureDate")
    @Mapping(target = "departure.airportName", source = "originIata")
    @Mapping(target = "departure.iata", source = "originIata")
    @Mapping(target = "departure.numberOfStops", source = "stops")
    @Mapping(target = "arrival.date", source = "arrivalDate")
    @Mapping(target = "arrival.airportName", source = "destinationIata")
    @Mapping(target = "arrival.iata", source = "destinationIata")
    @Mapping(target = "arrival.numberOfStops", source = "stops")
    @Mapping(target = "airline.name", constant = "airlineName")
    @Mapping(target = "airline.iata", constant = "airlineIata")
    @Mapping(target = "departure.location", constant = "departure.location")
    @Mapping(target = "departure.flightNumber", constant = "000000")
    @Mapping(target = "departure.seatClassDescription", constant = "departure.seatClassDescription")
    @Mapping(target = "arrival.location", constant = "arrival.location")
    @Mapping(target = "arrival.flightNumber", constant = "0000001")
    @Mapping(target = "arrival.seatClassDescription", constant = "arrival.seatClassDescription")
    FlightSummaryDTO segmentEntityToFlightSummaryDTO(SegmentEntity segmentEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO cancellationRuleEntityToServiceDTO(CancelationRuleEntity cancelationRuleEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO luggageEntityEntityToServiceDTO(LuggageEntity luggageEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO changeRuleEntityToServiceDTO(ChangeRuleEntity changeRuleEntity);

    @Mapping(target = "uom", constant = "uom")
    @Mapping(target = "quantity", constant = "111111")
    @Mapping(target = "weight", constant = "22222222")
    BaggageDTO luggageToBaggageDTO(LuggageEntity luggageEntity);

    default BaggageDTO setBaggage(Set<LuggageEntity> luggages) {
        var bagLuggage = luggages.stream().filter(luggage -> "TO_CHECK_IN".equals(luggage.getType())).toList().stream().findFirst();

        if (bagLuggage.isPresent()) {
            var mappedLuggage = luggageToBaggageDTO(bagLuggage.get());
            mappedLuggage.setIsIncluded(Boolean.TRUE);
            return mappedLuggage;
        }
        return BaggageDTO.builder().quantity(0).isIncluded(Boolean.FALSE).type("PIECE").build();
    }

    default ArrayList<ServiceDTO> mapServices(Set<CancelationRuleEntity> cancellationRules, Set<LuggageEntity> luggages, Set<ChangeRuleEntity> changeRules) {
        ArrayList<ServiceDTO> services = new ArrayList<ServiceDTO>();
        cancellationRules.forEach(rule -> services.add(cancellationRuleEntityToServiceDTO(rule)));
        luggages.forEach(luggage -> services.add(luggageEntityEntityToServiceDTO(luggage)));
        changeRules.forEach(rule -> services.add(changeRuleEntityToServiceDTO(rule)));
        return services;
    }

    default boolean isTaxItem(String skuId) {
        return skuId.toUpperCase().contains("TAX");
    }
    default List<ItemDTO> buildItemsDTO(OrderEntity order) {
        StatusDTO statusDTO = orderStatusEntityToStatusDTO(order.getCurrentStatus());

        var flight = order.getItems().stream().filter(item -> !isTaxItem(item.getSkuId())).toList();

        var travelSummary = TravelSummaryDTO.builder()
                .tour(null)
                .flights(buildFlights(flight.get(0).getSegments(), flight.get(0).getTravelInfo()))
                .accommodations(List.of())
                .vehicles(List.of())
                .services(List.of())
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

    @Mapping(target = "number", source = "phoneNumber")
    @Mapping(target = "localCode", source = "areaCode")
    @Mapping(target = "internationalCode", constant = "55")
    @Mapping(target = "type", constant = "_")
    Phone passengerToPhone(PaxEntity pax);

    default List<Phone> setPhone(PaxEntity pax) {
        return List.of(passengerToPhone(pax));
    }

    default List<FlightSummaryDTO> buildFlights(Set<SegmentEntity> segments, TravelInfoEntity travelInfo) {
        var gds = GlobalDistribuitionSystemDTO.builder()
                .reservationCode(travelInfo.getReservationCode())
//                TODO: remover apos testes
                .description("description")
                .provider("provider")
                .cancellationPolicies(List.of(CancellationPolicyDTO.builder().build()))
                .build();

        List<CustomerDTO> mappedPaxs = travelInfo.getPaxs().stream().map(this::paxEntityToCustomerDTO).toList();
        return segments.stream().map(segment -> {
            var flight = segmentEntityToFlightSummaryDTO(segment);
            flight.setPassengers(mappedPaxs);
            flight.setGlobalDistribuitionSystem(gds);
            return flight;
        }).toList();
    }
}
