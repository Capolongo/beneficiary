package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.*;
import br.com.livelo.orderflight.domain.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LiveloPartnersMapper {

    @Mapping(target = "amount", source = "price.amount")
    @Mapping(target = "currency", constant = "PTS")
    UpdateOrderDTO orderEntityToUpdateOrderDTO(OrderEntity order);

    @Mapping(source = "partnerOrderLinkId", target = "partnerOrderId")
    @Mapping(source = "skuId", target = "id")
    @Mapping(source = "price.amount", target = "price")
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

    CustomerDTO paxEntityToCustomerDTO(PaxEntity paxEntity);

    @Mapping(target = "doc", source = "documentNumber")
    @Mapping(target = "issuingDate", source = "issueDate")
    DocumentDTO documentEntityToDocumentDTO(DocumentEntity documentEntity);
    
    // ServiceDTO EntityToServiceDTO(Object Entity);
    // BaggageDTO EntityToBaggageDTO(Object Entity);
    // TourDTO EntityToTourDTO( Entity);
    
    @Mapping(target = "date", source = "departureDate")
    @Mapping(target = "airportName", source = "originIata")
    @Mapping(target = "iata", source = "originIata")
    DepartureArrivalDTO flightLegEntityToDepartureArrivalDTO(FlightLegEntity flightLegEntity);
    
    // DestinationDTO segmentEntityToDestinationDTO(SegmentEntity segmentEntity);
}
