package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.constants.AppConstants;
import br.com.livelo.orderflight.domain.dtos.update.*;
import br.com.livelo.orderflight.domain.entity.*;

import java.time.ZonedDateTime;
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
    @Mapping(target = "departureName", source = "originCity")
    @Mapping(target = "arrivalName", source = "destinationCity")
    @Mapping(target = "departure.airportName", source = "originAirport")
    @Mapping(target = "departure.cityName", source = "originCity")
    @Mapping(target = "arrival.airportName", source = "destinationAirport")
    @Mapping(target = "arrival.cityName", source = "destinationCity")
    @Mapping(target = "seatClassCode", source = "fareClass")
    @Mapping(target = "seatClassDescription", expression = "java(setSeatClass(flightLegEntity))")
    LegSummaryDTO flightLegEntityToLegSummaryDTO(FlightLegEntity flightLegEntity);

    @Mapping(target = "phones", expression = "java(setPhone(paxEntity))")
    @Mapping(target = "notes", constant = "notes")
    @Mapping(target = "documents", expression = "java(createDocumentsList(paxEntity.getDocuments()))")
    CustomerDTO paxEntityToCustomerDTO(PaxEntity paxEntity);

    default List<DocumentDTO> createDocumentsList(Set<DocumentEntity> docs) {
        return docs.stream().map(this::documentEntityToDocumentDTO).toList();
    }

    @Mapping(target = "doc", source = "documentNumber")
    @Mapping(target = "issuingDate", source = "issueDate")
    DocumentDTO documentEntityToDocumentDTO(DocumentEntity documentEntity);

    List<ServiceDTO> segmentEntityToServiceDTO(Set<SegmentEntity> segmentEntity);

    @Mapping(target = "services", expression = "java(mapServices(segmentEntity.getLuggages()))")
    @Mapping(target = "duration", source = "flightDuration")
    @Mapping(target = "legs", source = "flightsLegs")
    @Mapping(target = "baggage", expression = "java(setBaggage(segmentEntity.getLuggages()))")
    @Mapping(target = "departure.date", source = "departureDate")
    @Mapping(target = "departure.iata", source = "originIata")
    @Mapping(target = "departure.numberOfStops", source = "stops")
    @Mapping(target = "arrival.date", source = "arrivalDate")
    @Mapping(target = "arrival.iata", source = "destinationIata")
    @Mapping(target = "arrival.numberOfStops", source = "stops")
    @Mapping(target = "airline.name", source = "airlineDescription")
    @Mapping(target = "airline.iata", source = "airlineIata")
    @Mapping(target = "departure.location", source = "originCity")
    @Mapping(target = "departure.airportName", source = "originAirport")
    @Mapping(target = "departure.seatClassDescription", source = "cabinClass")
    @Mapping(target = "arrival.location", source = "destinationCity")
    @Mapping(target = "arrival.airportName", source = "destinationAirport")
    @Mapping(target = "arrival.seatClassDescription", source = "cabinClass")
    @Mapping(target = "departure.flightNumber", constant = "000000")
    @Mapping(target = "arrival.flightNumber", constant = "0000001")
    @Mapping(target = "isFlexible", constant = "false")
    FlightSummaryDTO segmentEntityToFlightSummaryDTO(SegmentEntity segmentEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO cancellationRuleEntityToServiceDTO(CancelationRuleEntity cancelationRuleEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO luggageEntityEntityToServiceDTO(LuggageEntity luggageEntity);

    @Mapping(target = "isIncluded", constant = "true")
    ServiceDTO changeRuleEntityToServiceDTO(ChangeRuleEntity changeRuleEntity);

    @Mapping(target = "uom", source = "measurement")
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

    default ArrayList<ServiceDTO> mapServices(Set<LuggageEntity> luggages) {
        ArrayList<ServiceDTO> services = new ArrayList<ServiceDTO>();
        var handLuggage = luggages.stream().filter(luggage -> AppConstants.HAND_BAGGAGE.equals(luggage.getType())).toList();
        if (!handLuggage.isEmpty()) {
            services.add(ServiceDTO.builder().type("HAND_LUGGAGE").isIncluded(Boolean.TRUE).description(handLuggage.get(0).getDescription()).build());
        }
        return services;
    }

    default boolean isTaxItem(String skuId) {
        return skuId.toUpperCase().contains("TAX");
    }

    default List<ItemDTO> buildItemsDTO(OrderEntity order) {
        StatusDTO statusDTO = orderStatusEntityToStatusDTO(order.getCurrentStatus());

        var flight = order.getItems().stream().filter(item -> !isTaxItem(item.getSkuId())).toList();


        var zoneDTO = ZoneDTO.builder()
                .departureDate(ZonedDateTime.now())
                .arrivalDate(ZonedDateTime.now())
                .description("desc")
                .longitude("123123")
                .latitude("1231231")
                .name("name")
                .build();
        var originDTO = OriginDTO.builder().departureDate("2024-12-12").arrivalDate("2024-12-12").zone(zoneDTO).build();
        var destinationDTO = DestinationDTO.builder().departureDate("2024-12-12").arrivalDate("2024-12-12").zone(zoneDTO).build();
        var tourDTO = TourDTO.builder()
                .description("tourDTO")
                .destinations(List.of(destinationDTO))
                .origins(List.of(originDTO))
                .build();

        var travelSummary = TravelSummaryDTO.builder()
                .tour(tourDTO)
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
            if (!isTaxItem(item.getSkuId()) && flight.get(0).getTravelInfo().getVoucher() != null) {
                if (mappedItem.getDocuments() == null) {
                    mappedItem.setDocuments(new ArrayList<>());
                }
                var voucher = ItemDocumentDTO
                        .builder()
                        .url(flight.get(0).getTravelInfo().getVoucher())
                        .code("code")
                        .descriptor("descriptor")
                        .name("Voucher de Viagem")
                        .type("voucher")
                        .build();
                mappedItem.getDocuments().add(voucher);

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
                .description("description")
                .cancellationPolicies(List.of(CancellationPolicyDTO.builder().build()))
                .build();

        List<CustomerDTO> mappedPaxs = travelInfo.getPaxs().stream().map(this::paxEntityToCustomerDTO).toList();
        var flights = segments.stream().map((segment) -> {
            gds.setProvider(segment.getAirlineDescription());
            var flight = segmentEntityToFlightSummaryDTO(segment);
            flight.setPassengers(mappedPaxs);
            flight.setGlobalDistribuitionSystem(gds);
            return flight;
        }).toList();

        flights.get(0).setRoute("GO");

        if (flights.size() > 1) {
            flights.get(1).setRoute("BACK");
        }

        return flights;
    }


    default String setSeatClass(FlightLegEntity leg) {
        return "ECONOMY".equalsIgnoreCase(leg.getFareBasis()) ? "Econômica" : "Executiva";
    }
}
