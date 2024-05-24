package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.ArrivalDTO;
import br.com.livelo.orderflight.domain.dtos.update.BaggageDTO;
import br.com.livelo.orderflight.domain.dtos.update.CustomerDTO;
import br.com.livelo.orderflight.domain.dtos.update.DepartureArrivalSummaryDTO;
import br.com.livelo.orderflight.domain.dtos.update.DepartureDTO;
import br.com.livelo.orderflight.domain.dtos.update.DocumentDTO;
import br.com.livelo.orderflight.domain.dtos.update.FlightSummaryDTO;
import br.com.livelo.orderflight.domain.dtos.update.ItemDTO;
import br.com.livelo.orderflight.domain.dtos.update.LegSummaryDTO;
import br.com.livelo.orderflight.domain.dtos.update.Phone;
import br.com.livelo.orderflight.domain.dtos.update.ServiceDTO;
import br.com.livelo.orderflight.domain.dtos.update.StatusDTO;
import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.dtos.update.ValidatingAirlineDTO;
import br.com.livelo.orderflight.domain.entity.CancellationRuleEntity;
import br.com.livelo.orderflight.domain.entity.ChangeRuleEntity;
import br.com.livelo.orderflight.domain.entity.DocumentEntity;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import br.com.livelo.orderflight.domain.entity.LuggageEntity;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class LiveloPartnersMapperImpl implements LiveloPartnersMapper {

    @Override
    public UpdateOrderDTO orderEntityToUpdateOrderDTO(OrderEntity order) {
        if ( order == null ) {
            return null;
        }

        UpdateOrderDTO.UpdateOrderDTOBuilder updateOrderDTO = UpdateOrderDTO.builder();

        BigDecimal amount = orderPriceAmount( order );
        if ( amount != null ) {
            updateOrderDTO.amount( amount.longValue() );
        }
        updateOrderDTO.partnerCode( order.getPartnerCode() );

        updateOrderDTO.currency( "PTS" );
        updateOrderDTO.items( buildItemsDTO(order) );

        return updateOrderDTO.build();
    }

    @Override
    public ItemDTO orderItemEntityToItemDTO(OrderItemEntity orderItemEntity) {
        if ( orderItemEntity == null ) {
            return null;
        }

        ItemDTO.ItemDTOBuilder itemDTO = ItemDTO.builder();

        itemDTO.partnerOrderId( orderItemEntity.getPartnerOrderLinkId() );
        itemDTO.id( orderItemEntity.getSkuId() );
        BigDecimal amount = orderItemEntityPriceAmount( orderItemEntity );
        if ( amount != null ) {
            itemDTO.price( amount.longValue() );
        }
        itemDTO.commerceItemId( orderItemEntity.getCommerceItemId() );
        if ( orderItemEntity.getQuantity() != null ) {
            itemDTO.quantity( orderItemEntity.getQuantity().longValue() );
        }

        itemDTO.currency( "PTS" );
        itemDTO.deliveryDate( "deliveryDate" );
        itemDTO.forceUpdate( isTaxItem( "forceUpdate" ) );

        return itemDTO.build();
    }

    @Override
    public StatusDTO orderStatusEntityToStatusDTO(OrderCurrentStatusEntity orderCurrentStatus) {
        if ( orderCurrentStatus == null ) {
            return null;
        }

        StatusDTO.StatusDTOBuilder statusDTO = StatusDTO.builder();

        statusDTO.message( orderCurrentStatus.getDescription() );
        statusDTO.details( orderCurrentStatus.getPartnerDescription() );
        statusDTO.code( orderCurrentStatus.getCode() );

        return statusDTO.build();
    }

    @Override
    public LegSummaryDTO flightLegEntityToLegSummaryDTO(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        LegSummaryDTO.LegSummaryDTOBuilder legSummaryDTO = LegSummaryDTO.builder();

        legSummaryDTO.managedAirline( flightLegEntityToValidatingAirlineDTO( flightLegEntity ) );
        legSummaryDTO.operationAirline( flightLegEntityToValidatingAirlineDTO1( flightLegEntity ) );
        legSummaryDTO.departure( flightLegEntityToDepartureArrivalSummaryDTO( flightLegEntity ) );
        legSummaryDTO.arrival( flightLegEntityToDepartureArrivalSummaryDTO1( flightLegEntity ) );
        legSummaryDTO.duration( flightLegEntity.getFlightDuration() );
        legSummaryDTO.departureName( flightLegEntity.getOriginCity() );
        legSummaryDTO.arrivalName( flightLegEntity.getDestinationCity() );
        legSummaryDTO.seatClassCode( flightLegEntity.getFareClass() );
        if ( flightLegEntity.getFlightNumber() != null ) {
            legSummaryDTO.flightNumber( Integer.parseInt( flightLegEntity.getFlightNumber() ) );
        }
        legSummaryDTO.aircraftCode( flightLegEntity.getAircraftCode() );

        legSummaryDTO.seatClassDescription( setSeatClass(flightLegEntity) );
        legSummaryDTO.stops( stopsList );

        return legSummaryDTO.build();
    }

    @Override
    public CustomerDTO paxEntityToCustomerDTO(PaxEntity paxEntity) {
        if ( paxEntity == null ) {
            return null;
        }

        CustomerDTO.CustomerDTOBuilder customerDTO = CustomerDTO.builder();

        if ( paxEntity.getId() != null ) {
            customerDTO.id( String.valueOf( paxEntity.getId() ) );
        }
        customerDTO.firstName( paxEntity.getFirstName() );
        customerDTO.lastName( paxEntity.getLastName() );
        customerDTO.gender( paxEntity.getGender() );
        customerDTO.email( paxEntity.getEmail() );
        customerDTO.birthDate( paxEntity.getBirthDate() );
        customerDTO.type( paxEntity.getType() );

        customerDTO.phones( setPhone(paxEntity) );
        customerDTO.notes( "notes" );
        customerDTO.documents( createDocumentsList(paxEntity.getDocuments()) );

        return customerDTO.build();
    }

    @Override
    public DocumentDTO documentEntityToDocumentDTO(DocumentEntity documentEntity) {
        if ( documentEntity == null ) {
            return null;
        }

        DocumentDTO.DocumentDTOBuilder documentDTO = DocumentDTO.builder();

        documentDTO.doc( documentEntity.getDocumentNumber() );
        documentDTO.issuingDate( documentEntity.getIssueDate() );
        documentDTO.type( documentEntity.getType() );
        documentDTO.issuingCountry( documentEntity.getIssuingCountry() );
        documentDTO.expirationDate( documentEntity.getExpirationDate() );
        documentDTO.residenceCountry( documentEntity.getResidenceCountry() );

        return documentDTO.build();
    }

    @Override
    public List<ServiceDTO> segmentEntityToServiceDTO(Set<SegmentEntity> segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        List<ServiceDTO> list = new ArrayList<ServiceDTO>( segmentEntity.size() );
        for ( SegmentEntity segmentEntity1 : segmentEntity ) {
            list.add( segmentEntityToServiceDTO1( segmentEntity1 ) );
        }

        return list;
    }

    @Override
    public FlightSummaryDTO segmentEntityToFlightSummaryDTO(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        FlightSummaryDTO.FlightSummaryDTOBuilder flightSummaryDTO = FlightSummaryDTO.builder();

        flightSummaryDTO.departure( segmentEntityToDepartureDTO( segmentEntity ) );
        flightSummaryDTO.arrival( segmentEntityToArrivalDTO( segmentEntity ) );
        flightSummaryDTO.airline( segmentEntityToValidatingAirlineDTO( segmentEntity ) );
        flightSummaryDTO.duration( segmentEntity.getFlightDuration() );
        flightSummaryDTO.legs( flightLegEntitySetToLegSummaryDTOList( segmentEntity.getFlightsLegs() ) );

        flightSummaryDTO.services( mapServices(segmentEntity.getLuggages()) );
        flightSummaryDTO.baggage( setBaggage(segmentEntity.getLuggages()) );
        flightSummaryDTO.isFlexible( false );

        return flightSummaryDTO.build();
    }

    @Override
    public ServiceDTO cancellationRuleEntityToServiceDTO(CancellationRuleEntity cancellationRuleEntity) {
        if ( cancellationRuleEntity == null ) {
            return null;
        }

        ServiceDTO.ServiceDTOBuilder serviceDTO = ServiceDTO.builder();

        serviceDTO.type( cancellationRuleEntity.getType() );
        serviceDTO.description( cancellationRuleEntity.getDescription() );

        serviceDTO.isIncluded( true );

        return serviceDTO.build();
    }

    @Override
    public ServiceDTO luggageEntityEntityToServiceDTO(LuggageEntity luggageEntity) {
        if ( luggageEntity == null ) {
            return null;
        }

        ServiceDTO.ServiceDTOBuilder serviceDTO = ServiceDTO.builder();

        serviceDTO.type( luggageEntity.getType() );
        serviceDTO.description( luggageEntity.getDescription() );

        serviceDTO.isIncluded( true );

        return serviceDTO.build();
    }

    @Override
    public ServiceDTO changeRuleEntityToServiceDTO(ChangeRuleEntity changeRuleEntity) {
        if ( changeRuleEntity == null ) {
            return null;
        }

        ServiceDTO.ServiceDTOBuilder serviceDTO = ServiceDTO.builder();

        serviceDTO.type( changeRuleEntity.getType() );
        serviceDTO.description( changeRuleEntity.getDescription() );

        serviceDTO.isIncluded( true );

        return serviceDTO.build();
    }

    @Override
    public BaggageDTO luggageToBaggageDTO(LuggageEntity luggageEntity) {
        if ( luggageEntity == null ) {
            return null;
        }

        BaggageDTO.BaggageDTOBuilder baggageDTO = BaggageDTO.builder();

        baggageDTO.uom( luggageEntity.getMeasurement() );
        baggageDTO.type( luggageEntity.getType() );
        if ( luggageEntity.getQuantity() != null ) {
            baggageDTO.quantity( luggageEntity.getQuantity().intValue() );
        }
        if ( luggageEntity.getWeight() != null ) {
            baggageDTO.weight( luggageEntity.getWeight().intValue() );
        }

        return baggageDTO.build();
    }

    @Override
    public Phone passengerToPhone(PaxEntity pax) {
        if ( pax == null ) {
            return null;
        }

        Phone.PhoneBuilder phone = Phone.builder();

        phone.number( pax.getPhoneNumber() );
        if ( pax.getAreaCode() != null ) {
            phone.localCode( Integer.parseInt( pax.getAreaCode() ) );
        }

        phone.internationalCode( 55 );
        phone.type( "_" );

        return phone.build();
    }

    private BigDecimal orderPriceAmount(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }
        OrderPriceEntity price = orderEntity.getPrice();
        if ( price == null ) {
            return null;
        }
        BigDecimal amount = price.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }

    private BigDecimal orderItemEntityPriceAmount(OrderItemEntity orderItemEntity) {
        if ( orderItemEntity == null ) {
            return null;
        }
        OrderItemPriceEntity price = orderItemEntity.getPrice();
        if ( price == null ) {
            return null;
        }
        BigDecimal amount = price.getAmount();
        if ( amount == null ) {
            return null;
        }
        return amount;
    }

    protected ValidatingAirlineDTO flightLegEntityToValidatingAirlineDTO(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        ValidatingAirlineDTO.ValidatingAirlineDTOBuilder validatingAirlineDTO = ValidatingAirlineDTO.builder();

        validatingAirlineDTO.name( flightLegEntity.getAirlineManagedByDescription() );
        validatingAirlineDTO.iata( flightLegEntity.getAirlineManagedByIata() );

        return validatingAirlineDTO.build();
    }

    protected ValidatingAirlineDTO flightLegEntityToValidatingAirlineDTO1(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        ValidatingAirlineDTO.ValidatingAirlineDTOBuilder validatingAirlineDTO = ValidatingAirlineDTO.builder();

        validatingAirlineDTO.name( flightLegEntity.getAirlineOperatedByDescription() );
        validatingAirlineDTO.iata( flightLegEntity.getAirlineOperatedByIata() );

        return validatingAirlineDTO.build();
    }

    protected DepartureArrivalSummaryDTO flightLegEntityToDepartureArrivalSummaryDTO(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        DepartureArrivalSummaryDTO.DepartureArrivalSummaryDTOBuilder departureArrivalSummaryDTO = DepartureArrivalSummaryDTO.builder();

        if ( flightLegEntity.getDepartureDate() != null ) {
            departureArrivalSummaryDTO.date( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( flightLegEntity.getDepartureDate() ) );
        }
        departureArrivalSummaryDTO.iata( flightLegEntity.getOriginIata() );
        departureArrivalSummaryDTO.airportName( flightLegEntity.getOriginAirport() );
        departureArrivalSummaryDTO.cityName( flightLegEntity.getOriginCity() );

        return departureArrivalSummaryDTO.build();
    }

    protected DepartureArrivalSummaryDTO flightLegEntityToDepartureArrivalSummaryDTO1(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        DepartureArrivalSummaryDTO.DepartureArrivalSummaryDTOBuilder departureArrivalSummaryDTO = DepartureArrivalSummaryDTO.builder();

        departureArrivalSummaryDTO.iata( flightLegEntity.getDestinationIata() );
        if ( flightLegEntity.getArrivalDate() != null ) {
            departureArrivalSummaryDTO.date( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( flightLegEntity.getArrivalDate() ) );
        }
        departureArrivalSummaryDTO.airportName( flightLegEntity.getDestinationAirport() );
        departureArrivalSummaryDTO.cityName( flightLegEntity.getDestinationCity() );

        return departureArrivalSummaryDTO.build();
    }

    protected ServiceDTO segmentEntityToServiceDTO1(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        ServiceDTO.ServiceDTOBuilder serviceDTO = ServiceDTO.builder();

        return serviceDTO.build();
    }

    protected DepartureDTO segmentEntityToDepartureDTO(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        DepartureDTO.DepartureDTOBuilder departureDTO = DepartureDTO.builder();

        if ( segmentEntity.getDepartureDate() != null ) {
            departureDTO.date( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( segmentEntity.getDepartureDate() ) );
        }
        departureDTO.iata( segmentEntity.getOriginIata() );
        departureDTO.numberOfStops( segmentEntity.getStops() );
        departureDTO.location( segmentEntity.getOriginCity() );
        departureDTO.airportName( segmentEntity.getOriginAirport() );
        departureDTO.seatClassDescription( segmentEntity.getCabinClass() );

        departureDTO.flightNumber( 000000 );

        return departureDTO.build();
    }

    protected ArrivalDTO segmentEntityToArrivalDTO(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        ArrivalDTO.ArrivalDTOBuilder arrivalDTO = ArrivalDTO.builder();

        if ( segmentEntity.getArrivalDate() != null ) {
            arrivalDTO.date( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( segmentEntity.getArrivalDate() ) );
        }
        arrivalDTO.iata( segmentEntity.getDestinationIata() );
        arrivalDTO.numberOfStops( segmentEntity.getStops() );
        arrivalDTO.location( segmentEntity.getDestinationCity() );
        arrivalDTO.airportName( segmentEntity.getDestinationAirport() );
        arrivalDTO.seatClassDescription( segmentEntity.getCabinClass() );

        arrivalDTO.flightNumber( 0000001 );

        return arrivalDTO.build();
    }

    protected ValidatingAirlineDTO segmentEntityToValidatingAirlineDTO(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        ValidatingAirlineDTO.ValidatingAirlineDTOBuilder validatingAirlineDTO = ValidatingAirlineDTO.builder();

        validatingAirlineDTO.name( segmentEntity.getAirlineDescription() );
        validatingAirlineDTO.iata( segmentEntity.getAirlineIata() );

        return validatingAirlineDTO.build();
    }

    protected List<LegSummaryDTO> flightLegEntitySetToLegSummaryDTOList(Set<FlightLegEntity> set) {
        if ( set == null ) {
            return null;
        }

        List<LegSummaryDTO> list = new ArrayList<LegSummaryDTO>( set.size() );
        for ( FlightLegEntity flightLegEntity : set ) {
            list.add( flightLegEntityToLegSummaryDTO( flightLegEntity ) );
        }

        return list;
    }
}
