package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderItemResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderPriceResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderStatusResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderCancaletionRulesResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderChangeRulesResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderDocumentResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderFlightsLegsResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderLuggagesResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderPaxResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderSegmentsResponse;
import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmationOrderTravelInfoResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmDocumentRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderPaxRequest;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequest;
import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderStatusResponse;
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
import br.com.livelo.orderflight.domain.entity.OrderStatusHistoryEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ConfirmOrderMapperImpl implements ConfirmOrderMapper {

    @Override
    public ConfirmOrderItemResponse orderItemEntityToConfirmOrderItemResponse(OrderItemEntity orderItemEntity) {
        if ( orderItemEntity == null ) {
            return null;
        }

        ConfirmOrderItemResponse.ConfirmOrderItemResponseBuilder confirmOrderItemResponse = ConfirmOrderItemResponse.builder();

        confirmOrderItemResponse.productType( orderItemEntity.getProductId() );
        confirmOrderItemResponse.commerceItemId( orderItemEntity.getCommerceItemId() );
        confirmOrderItemResponse.skuId( orderItemEntity.getSkuId() );
        confirmOrderItemResponse.productId( orderItemEntity.getProductId() );
        confirmOrderItemResponse.externalCoupon( orderItemEntity.getExternalCoupon() );
        confirmOrderItemResponse.price( orderItemPriceEntityToConfirmOrderPriceResponse( orderItemEntity.getPrice() ) );
        confirmOrderItemResponse.travelInfo( travelInfoEntityToConfirmationOrderTravelInfoResponse( orderItemEntity.getTravelInfo() ) );
        confirmOrderItemResponse.segments( segmentEntitySetToConfirmationOrderSegmentsResponseSet( orderItemEntity.getSegments() ) );

        return confirmOrderItemResponse.build();
    }

    @Override
    public ConfirmationOrderFlightsLegsResponse flightLegEntityToConfirmationOrderFlightsLegsResponse(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        ConfirmationOrderFlightsLegsResponse.ConfirmationOrderFlightsLegsResponseBuilder confirmationOrderFlightsLegsResponse = ConfirmationOrderFlightsLegsResponse.builder();

        confirmationOrderFlightsLegsResponse.operatedBy( flightLegEntity.getAirlineOperatedByIata() );
        confirmationOrderFlightsLegsResponse.flightNumber( flightLegEntity.getFlightNumber() );
        if ( flightLegEntity.getFlightDuration() != null ) {
            confirmationOrderFlightsLegsResponse.flightDuration( flightLegEntity.getFlightDuration() );
        }
        if ( flightLegEntity.getTimeToWait() != null ) {
            confirmationOrderFlightsLegsResponse.timeToWait( String.valueOf( flightLegEntity.getTimeToWait() ) );
        }
        confirmationOrderFlightsLegsResponse.originIata( flightLegEntity.getOriginIata() );
        confirmationOrderFlightsLegsResponse.destinationIata( flightLegEntity.getDestinationIata() );
        confirmationOrderFlightsLegsResponse.departureDate( flightLegEntity.getDepartureDate() );
        confirmationOrderFlightsLegsResponse.arrivalDate( flightLegEntity.getArrivalDate() );
        confirmationOrderFlightsLegsResponse.type( flightLegEntity.getType() );

        return confirmationOrderFlightsLegsResponse.build();
    }

    @Override
    public ConfirmationOrderDocumentResponse paxEntityToConfirmationOrderPaxResponse(DocumentEntity documentEntity) {
        if ( documentEntity == null ) {
            return null;
        }

        ConfirmationOrderDocumentResponse.ConfirmationOrderDocumentResponseBuilder confirmationOrderDocumentResponse = ConfirmationOrderDocumentResponse.builder();

        confirmationOrderDocumentResponse.id( documentEntity.getId() );
        confirmationOrderDocumentResponse.documentNumber( documentEntity.getDocumentNumber() );
        confirmationOrderDocumentResponse.type( documentEntity.getType() );
        confirmationOrderDocumentResponse.issueDate( documentEntity.getIssueDate() );
        confirmationOrderDocumentResponse.issuingCountry( documentEntity.getIssuingCountry() );
        confirmationOrderDocumentResponse.expirationDate( documentEntity.getExpirationDate() );
        confirmationOrderDocumentResponse.residenceCountry( documentEntity.getResidenceCountry() );

        return confirmationOrderDocumentResponse.build();
    }

    @Override
    public ConfirmationOrderPaxResponse paxEntityToConfirmationOrderPaxResponse(PaxEntity paxEntity) {
        if ( paxEntity == null ) {
            return null;
        }

        ConfirmationOrderPaxResponse.ConfirmationOrderPaxResponseBuilder confirmationOrderPaxResponse = ConfirmationOrderPaxResponse.builder();

        confirmationOrderPaxResponse.phone( paxEntity.getPhoneNumber() );
        confirmationOrderPaxResponse.type( paxEntity.getType() );
        confirmationOrderPaxResponse.firstName( paxEntity.getFirstName() );
        confirmationOrderPaxResponse.lastName( paxEntity.getLastName() );
        confirmationOrderPaxResponse.birthDate( paxEntity.getBirthDate() );
        confirmationOrderPaxResponse.documents( documentEntitySetToConfirmationOrderDocumentResponseSet( paxEntity.getDocuments() ) );
        confirmationOrderPaxResponse.email( paxEntity.getEmail() );
        confirmationOrderPaxResponse.areaCode( paxEntity.getAreaCode() );

        return confirmationOrderPaxResponse.build();
    }

    @Override
    public ConfirmOrderResponse orderEntityToConfirmOrderResponse(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }

        ConfirmOrderResponse.ConfirmOrderResponseBuilder confirmOrderResponse = ConfirmOrderResponse.builder();

        confirmOrderResponse.status( orderCurrentStatusEntityToConfirmOrderStatusResponse( orderEntity.getCurrentStatus() ) );
        confirmOrderResponse.id( orderEntity.getId() );
        confirmOrderResponse.commerceOrderId( orderEntity.getCommerceOrderId() );
        confirmOrderResponse.partnerOrderId( orderEntity.getPartnerOrderId() );
        confirmOrderResponse.partnerCode( orderEntity.getPartnerCode() );
        if ( orderEntity.getSubmittedDate() != null ) {
            confirmOrderResponse.submittedDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( orderEntity.getSubmittedDate() ) );
        }
        if ( orderEntity.getExpirationDate() != null ) {
            confirmOrderResponse.expirationDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( orderEntity.getExpirationDate() ) );
        }
        confirmOrderResponse.transactionId( orderEntity.getTransactionId() );
        confirmOrderResponse.price( orderPriceEntityToConfirmOrderPriceResponse( orderEntity.getPrice() ) );
        confirmOrderResponse.items( orderItemEntitySetToConfirmOrderItemResponseSet( orderEntity.getItems() ) );

        return confirmOrderResponse.build();
    }

    @Override
    public ConnectorConfirmOrderRequest orderEntityToConnectorConfirmOrderRequest(OrderEntity orderEntity) {
        if ( orderEntity == null ) {
            return null;
        }

        ConnectorConfirmOrderRequest.ConnectorConfirmOrderRequestBuilder connectorConfirmOrderRequest = ConnectorConfirmOrderRequest.builder();

        connectorConfirmOrderRequest.id( orderEntity.getId() );
        connectorConfirmOrderRequest.commerceOrderId( orderEntity.getCommerceOrderId() );
        connectorConfirmOrderRequest.partnerOrderId( orderEntity.getPartnerOrderId() );
        connectorConfirmOrderRequest.partnerCode( orderEntity.getPartnerCode() );
        if ( orderEntity.getSubmittedDate() != null ) {
            connectorConfirmOrderRequest.submittedDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( orderEntity.getSubmittedDate() ) );
        }
        if ( orderEntity.getExpirationDate() != null ) {
            connectorConfirmOrderRequest.expirationDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( orderEntity.getExpirationDate() ) );
        }

        connectorConfirmOrderRequest.commerceItemId( getFlightItemCommerceItemId(orderEntity) );
        connectorConfirmOrderRequest.partnerOrderLinkId( getFlightItemPartnerOrderLinkId(orderEntity) );
        connectorConfirmOrderRequest.paxs( reducePaxs(orderEntity) );
        connectorConfirmOrderRequest.segmentsPartnerIds( setSegmentsPartnersIds(orderEntity) );

        return connectorConfirmOrderRequest.build();
    }

    @Override
    public OrderStatusHistoryEntity statusHistoryToCurrentStatus(OrderCurrentStatusEntity orderCurrentStatusEntity) {
        if ( orderCurrentStatusEntity == null ) {
            return null;
        }

        OrderStatusHistoryEntity.OrderStatusHistoryEntityBuilder orderStatusHistoryEntity = OrderStatusHistoryEntity.builder();

        orderStatusHistoryEntity.id( orderCurrentStatusEntity.getId() );
        orderStatusHistoryEntity.code( orderCurrentStatusEntity.getCode() );
        orderStatusHistoryEntity.description( orderCurrentStatusEntity.getDescription() );
        orderStatusHistoryEntity.partnerCode( orderCurrentStatusEntity.getPartnerCode() );
        orderStatusHistoryEntity.partnerDescription( orderCurrentStatusEntity.getPartnerDescription() );
        orderStatusHistoryEntity.partnerResponse( orderCurrentStatusEntity.getPartnerResponse() );

        return orderStatusHistoryEntity.build();
    }

    @Override
    public ConnectorConfirmOrderPaxRequest paxEntityToConnectorConfirmOrderPaxRequest(PaxEntity pax) {
        if ( pax == null ) {
            return null;
        }

        ConnectorConfirmOrderPaxRequest.ConnectorConfirmOrderPaxRequestBuilder connectorConfirmOrderPaxRequest = ConnectorConfirmOrderPaxRequest.builder();

        connectorConfirmOrderPaxRequest.type( pax.getType() );
        connectorConfirmOrderPaxRequest.firstName( pax.getFirstName() );
        connectorConfirmOrderPaxRequest.lastName( pax.getLastName() );
        connectorConfirmOrderPaxRequest.gender( pax.getGender() );
        connectorConfirmOrderPaxRequest.birthDate( pax.getBirthDate() );
        connectorConfirmOrderPaxRequest.email( pax.getEmail() );
        connectorConfirmOrderPaxRequest.areaCode( pax.getAreaCode() );
        connectorConfirmOrderPaxRequest.phoneNumber( pax.getPhoneNumber() );
        connectorConfirmOrderPaxRequest.documents( documentEntitySetToConnectorConfirmDocumentRequestSet( pax.getDocuments() ) );

        return connectorConfirmOrderPaxRequest.build();
    }

    @Override
    public ConnectorConfirmDocumentRequest documentEntityToConnectorConfirmDocumentRequest(DocumentEntity document) {
        if ( document == null ) {
            return null;
        }

        ConnectorConfirmDocumentRequest.ConnectorConfirmDocumentRequestBuilder connectorConfirmDocumentRequest = ConnectorConfirmDocumentRequest.builder();

        connectorConfirmDocumentRequest.number( document.getDocumentNumber() );
        connectorConfirmDocumentRequest.type( document.getType() );

        return connectorConfirmDocumentRequest.build();
    }

    @Override
    public OrderCurrentStatusEntity connectorConfirmOrderStatusResponseToStatusEntity(ConnectorConfirmOrderStatusResponse connectorConfirmOrderStatusResponse) {
        if ( connectorConfirmOrderStatusResponse == null ) {
            return null;
        }

        OrderCurrentStatusEntity.OrderCurrentStatusEntityBuilder orderCurrentStatusEntity = OrderCurrentStatusEntity.builder();

        orderCurrentStatusEntity.id( connectorConfirmOrderStatusResponse.getId() );
        orderCurrentStatusEntity.code( connectorConfirmOrderStatusResponse.getCode() );
        orderCurrentStatusEntity.description( connectorConfirmOrderStatusResponse.getDescription() );
        orderCurrentStatusEntity.partnerCode( connectorConfirmOrderStatusResponse.getPartnerCode() );
        orderCurrentStatusEntity.partnerDescription( connectorConfirmOrderStatusResponse.getPartnerDescription() );
        orderCurrentStatusEntity.partnerResponse( connectorConfirmOrderStatusResponse.getPartnerResponse() );

        return orderCurrentStatusEntity.build();
    }

    protected ConfirmOrderPriceResponse orderItemPriceEntityToConfirmOrderPriceResponse(OrderItemPriceEntity orderItemPriceEntity) {
        if ( orderItemPriceEntity == null ) {
            return null;
        }

        ConfirmOrderPriceResponse.ConfirmOrderPriceResponseBuilder confirmOrderPriceResponse = ConfirmOrderPriceResponse.builder();

        confirmOrderPriceResponse.amount( orderItemPriceEntity.getAmount() );
        confirmOrderPriceResponse.pointsAmount( orderItemPriceEntity.getPointsAmount() );

        return confirmOrderPriceResponse.build();
    }

    protected Set<ConfirmationOrderPaxResponse> paxEntitySetToConfirmationOrderPaxResponseSet(Set<PaxEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderPaxResponse> set1 = new LinkedHashSet<ConfirmationOrderPaxResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PaxEntity paxEntity : set ) {
            set1.add( paxEntityToConfirmationOrderPaxResponse( paxEntity ) );
        }

        return set1;
    }

    protected ConfirmationOrderTravelInfoResponse travelInfoEntityToConfirmationOrderTravelInfoResponse(TravelInfoEntity travelInfoEntity) {
        if ( travelInfoEntity == null ) {
            return null;
        }

        ConfirmationOrderTravelInfoResponse.ConfirmationOrderTravelInfoResponseBuilder confirmationOrderTravelInfoResponse = ConfirmationOrderTravelInfoResponse.builder();

        confirmationOrderTravelInfoResponse.type( travelInfoEntity.getType() );
        confirmationOrderTravelInfoResponse.reservationCode( travelInfoEntity.getReservationCode() );
        if ( travelInfoEntity.getAdt() != null ) {
            confirmationOrderTravelInfoResponse.adt( travelInfoEntity.getAdt() );
        }
        if ( travelInfoEntity.getChd() != null ) {
            confirmationOrderTravelInfoResponse.chd( travelInfoEntity.getChd() );
        }
        if ( travelInfoEntity.getInf() != null ) {
            confirmationOrderTravelInfoResponse.inf( travelInfoEntity.getInf() );
        }
        confirmationOrderTravelInfoResponse.cabinClass( travelInfoEntity.getCabinClass() );
        confirmationOrderTravelInfoResponse.voucher( travelInfoEntity.getVoucher() );
        confirmationOrderTravelInfoResponse.paxs( paxEntitySetToConfirmationOrderPaxResponseSet( travelInfoEntity.getPaxs() ) );

        return confirmationOrderTravelInfoResponse.build();
    }

    protected Set<ConfirmationOrderFlightsLegsResponse> flightLegEntitySetToConfirmationOrderFlightsLegsResponseSet(Set<FlightLegEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderFlightsLegsResponse> set1 = new LinkedHashSet<ConfirmationOrderFlightsLegsResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( FlightLegEntity flightLegEntity : set ) {
            set1.add( flightLegEntityToConfirmationOrderFlightsLegsResponse( flightLegEntity ) );
        }

        return set1;
    }

    protected ConfirmationOrderLuggagesResponse luggageEntityToConfirmationOrderLuggagesResponse(LuggageEntity luggageEntity) {
        if ( luggageEntity == null ) {
            return null;
        }

        ConfirmationOrderLuggagesResponse.ConfirmationOrderLuggagesResponseBuilder confirmationOrderLuggagesResponse = ConfirmationOrderLuggagesResponse.builder();

        confirmationOrderLuggagesResponse.type( luggageEntity.getType() );
        confirmationOrderLuggagesResponse.description( luggageEntity.getDescription() );

        return confirmationOrderLuggagesResponse.build();
    }

    protected Set<ConfirmationOrderLuggagesResponse> luggageEntitySetToConfirmationOrderLuggagesResponseSet(Set<LuggageEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderLuggagesResponse> set1 = new LinkedHashSet<ConfirmationOrderLuggagesResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( LuggageEntity luggageEntity : set ) {
            set1.add( luggageEntityToConfirmationOrderLuggagesResponse( luggageEntity ) );
        }

        return set1;
    }

    protected ConfirmationOrderCancaletionRulesResponse cancellationRuleEntityToConfirmationOrderCancaletionRulesResponse(CancellationRuleEntity cancellationRuleEntity) {
        if ( cancellationRuleEntity == null ) {
            return null;
        }

        ConfirmationOrderCancaletionRulesResponse.ConfirmationOrderCancaletionRulesResponseBuilder confirmationOrderCancaletionRulesResponse = ConfirmationOrderCancaletionRulesResponse.builder();

        confirmationOrderCancaletionRulesResponse.type( cancellationRuleEntity.getType() );
        confirmationOrderCancaletionRulesResponse.description( cancellationRuleEntity.getDescription() );

        return confirmationOrderCancaletionRulesResponse.build();
    }

    protected Set<ConfirmationOrderCancaletionRulesResponse> cancellationRuleEntitySetToConfirmationOrderCancaletionRulesResponseSet(Set<CancellationRuleEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderCancaletionRulesResponse> set1 = new LinkedHashSet<ConfirmationOrderCancaletionRulesResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( CancellationRuleEntity cancellationRuleEntity : set ) {
            set1.add( cancellationRuleEntityToConfirmationOrderCancaletionRulesResponse( cancellationRuleEntity ) );
        }

        return set1;
    }

    protected ConfirmationOrderChangeRulesResponse changeRuleEntityToConfirmationOrderChangeRulesResponse(ChangeRuleEntity changeRuleEntity) {
        if ( changeRuleEntity == null ) {
            return null;
        }

        ConfirmationOrderChangeRulesResponse.ConfirmationOrderChangeRulesResponseBuilder confirmationOrderChangeRulesResponse = ConfirmationOrderChangeRulesResponse.builder();

        confirmationOrderChangeRulesResponse.type( changeRuleEntity.getType() );
        confirmationOrderChangeRulesResponse.description( changeRuleEntity.getDescription() );

        return confirmationOrderChangeRulesResponse.build();
    }

    protected Set<ConfirmationOrderChangeRulesResponse> changeRuleEntitySetToConfirmationOrderChangeRulesResponseSet(Set<ChangeRuleEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderChangeRulesResponse> set1 = new LinkedHashSet<ConfirmationOrderChangeRulesResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ChangeRuleEntity changeRuleEntity : set ) {
            set1.add( changeRuleEntityToConfirmationOrderChangeRulesResponse( changeRuleEntity ) );
        }

        return set1;
    }

    protected ConfirmationOrderSegmentsResponse segmentEntityToConfirmationOrderSegmentsResponse(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        ConfirmationOrderSegmentsResponse.ConfirmationOrderSegmentsResponseBuilder confirmationOrderSegmentsResponse = ConfirmationOrderSegmentsResponse.builder();

        confirmationOrderSegmentsResponse.partnerId( segmentEntity.getPartnerId() );
        confirmationOrderSegmentsResponse.step( segmentEntity.getStep() );
        if ( segmentEntity.getStops() != null ) {
            confirmationOrderSegmentsResponse.stops( segmentEntity.getStops() );
        }
        if ( segmentEntity.getFlightDuration() != null ) {
            confirmationOrderSegmentsResponse.flightDuration( segmentEntity.getFlightDuration() );
        }
        confirmationOrderSegmentsResponse.originIata( segmentEntity.getOriginIata() );
        confirmationOrderSegmentsResponse.destinationIata( segmentEntity.getDestinationIata() );
        confirmationOrderSegmentsResponse.departureDate( segmentEntity.getDepartureDate() );
        confirmationOrderSegmentsResponse.arrivalDate( segmentEntity.getArrivalDate() );
        confirmationOrderSegmentsResponse.flightsLegs( flightLegEntitySetToConfirmationOrderFlightsLegsResponseSet( segmentEntity.getFlightsLegs() ) );
        confirmationOrderSegmentsResponse.luggages( luggageEntitySetToConfirmationOrderLuggagesResponseSet( segmentEntity.getLuggages() ) );
        confirmationOrderSegmentsResponse.cancellationRules( cancellationRuleEntitySetToConfirmationOrderCancaletionRulesResponseSet( segmentEntity.getCancellationRules() ) );
        confirmationOrderSegmentsResponse.changeRules( changeRuleEntitySetToConfirmationOrderChangeRulesResponseSet( segmentEntity.getChangeRules() ) );

        return confirmationOrderSegmentsResponse.build();
    }

    protected Set<ConfirmationOrderSegmentsResponse> segmentEntitySetToConfirmationOrderSegmentsResponseSet(Set<SegmentEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderSegmentsResponse> set1 = new LinkedHashSet<ConfirmationOrderSegmentsResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SegmentEntity segmentEntity : set ) {
            set1.add( segmentEntityToConfirmationOrderSegmentsResponse( segmentEntity ) );
        }

        return set1;
    }

    protected Set<ConfirmationOrderDocumentResponse> documentEntitySetToConfirmationOrderDocumentResponseSet(Set<DocumentEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmationOrderDocumentResponse> set1 = new LinkedHashSet<ConfirmationOrderDocumentResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( DocumentEntity documentEntity : set ) {
            set1.add( paxEntityToConfirmationOrderPaxResponse( documentEntity ) );
        }

        return set1;
    }

    protected ConfirmOrderStatusResponse orderCurrentStatusEntityToConfirmOrderStatusResponse(OrderCurrentStatusEntity orderCurrentStatusEntity) {
        if ( orderCurrentStatusEntity == null ) {
            return null;
        }

        ConfirmOrderStatusResponse.ConfirmOrderStatusResponseBuilder confirmOrderStatusResponse = ConfirmOrderStatusResponse.builder();

        confirmOrderStatusResponse.details( orderCurrentStatusEntity.getPartnerResponse() );
        confirmOrderStatusResponse.code( orderCurrentStatusEntity.getCode() );
        confirmOrderStatusResponse.description( orderCurrentStatusEntity.getDescription() );

        return confirmOrderStatusResponse.build();
    }

    protected ConfirmOrderPriceResponse orderPriceEntityToConfirmOrderPriceResponse(OrderPriceEntity orderPriceEntity) {
        if ( orderPriceEntity == null ) {
            return null;
        }

        ConfirmOrderPriceResponse.ConfirmOrderPriceResponseBuilder confirmOrderPriceResponse = ConfirmOrderPriceResponse.builder();

        confirmOrderPriceResponse.amount( orderPriceEntity.getAmount() );
        confirmOrderPriceResponse.pointsAmount( orderPriceEntity.getPointsAmount() );

        return confirmOrderPriceResponse.build();
    }

    protected Set<ConfirmOrderItemResponse> orderItemEntitySetToConfirmOrderItemResponseSet(Set<OrderItemEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConfirmOrderItemResponse> set1 = new LinkedHashSet<ConfirmOrderItemResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( OrderItemEntity orderItemEntity : set ) {
            set1.add( orderItemEntityToConfirmOrderItemResponse( orderItemEntity ) );
        }

        return set1;
    }

    protected Set<ConnectorConfirmDocumentRequest> documentEntitySetToConnectorConfirmDocumentRequestSet(Set<DocumentEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ConnectorConfirmDocumentRequest> set1 = new LinkedHashSet<ConnectorConfirmDocumentRequest>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( DocumentEntity documentEntity : set ) {
            set1.add( documentEntityToConnectorConfirmDocumentRequest( documentEntity ) );
        }

        return set1;
    }
}
