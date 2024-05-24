package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationDocument;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationPax;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationDocument;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationPax;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseCancellationRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseChangeRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseDocument;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseFlightLeg;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseItem;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseLuggage;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseOrderItemPrice;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseOrderStatus;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponsePax;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponsePrice;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponsePriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponsePriceModality;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseSegment;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponseTravelInfo;
import br.com.livelo.orderflight.domain.entity.CancellationRuleEntity;
import br.com.livelo.orderflight.domain.entity.ChangeRuleEntity;
import br.com.livelo.orderflight.domain.entity.DocumentEntity;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import br.com.livelo.orderflight.domain.entity.LuggageEntity;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemPriceEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceDescriptionEntity;
import br.com.livelo.orderflight.domain.entity.OrderPriceEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import br.com.livelo.orderflight.domain.entity.PriceModalityEntity;
import br.com.livelo.orderflight.domain.entity.SegmentEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public OrderEntity toOrderEntity(ReservationRequest reservationRequest, PartnerReservationResponse partnerReservationResponse, String transactionId, String customerId, String channel, String listPrice) {
        if ( reservationRequest == null && partnerReservationResponse == null && transactionId == null && customerId == null && channel == null && listPrice == null ) {
            return null;
        }

        OrderEntity.OrderEntityBuilder<?, ?> orderEntity = OrderEntity.builder();

        if ( reservationRequest != null ) {
            orderEntity.commerceOrderId( reservationRequest.getCommerceOrderId() );
            orderEntity.partnerCode( reservationRequest.getPartnerCode() );
        }
        if ( partnerReservationResponse != null ) {
            orderEntity.expirationDate( partnerReservationResponse.getExpirationDate() );
            orderEntity.partnerOrderId( partnerReservationResponse.getPartnerOrderId() );
        }
        orderEntity.transactionId( transactionId );
        orderEntity.customerIdentifier( customerId );
        orderEntity.channel( channel );
        orderEntity.items( mapItems(reservationRequest, partnerReservationResponse, listPrice) );
        orderEntity.statusHistory( Set.of(mapStatusHistory(partnerReservationResponse)) );
        orderEntity.currentStatus( mapStatus(partnerReservationResponse) );
        orderEntity.price( mapPrice(partnerReservationResponse, listPrice) );

        return orderEntity.build();
    }

    @Override
    public PartnerReservationRequest toPartnerReservationRequest(ReservationRequest request) {
        if ( request == null ) {
            return null;
        }

        PartnerReservationRequest.PartnerReservationRequestBuilder partnerReservationRequest = PartnerReservationRequest.builder();

        partnerReservationRequest.segmentsPartnerIds( mapSegmentsPartnerdIds( request.getSegmentsPartnerIds() ) );
        partnerReservationRequest.commerceOrderId( request.getCommerceOrderId() );
        partnerReservationRequest.items( reservationItemListToPartnerReservationItemList( request.getItems() ) );
        partnerReservationRequest.paxs( reservationPaxListToPartnerReservationPaxList( request.getPaxs() ) );

        return partnerReservationRequest.build();
    }

    @Override
    public PartnerReservationDocument toPartnerReservationDocument(ReservationDocument reservationDocument) {
        if ( reservationDocument == null ) {
            return null;
        }

        PartnerReservationDocument.PartnerReservationDocumentBuilder partnerReservationDocument = PartnerReservationDocument.builder();

        partnerReservationDocument.number( reservationDocument.getDocumentNumber() );
        partnerReservationDocument.type( reservationDocument.getType() );
        partnerReservationDocument.issueDate( reservationDocument.getIssueDate() );
        partnerReservationDocument.issuingCountry( reservationDocument.getIssuingCountry() );
        partnerReservationDocument.expirationDate( reservationDocument.getExpirationDate() );
        partnerReservationDocument.residenceCountry( reservationDocument.getResidenceCountry() );

        return partnerReservationDocument.build();
    }

    @Override
    public ReservationResponse toReservationResponse(OrderEntity orderEntity, int expirationTimer) {
        if ( orderEntity == null ) {
            return null;
        }

        String orderId = null;
        String commerceOrderId = null;
        String partnerOrderId = null;
        String partnerCode = null;
        LocalDateTime submittedDate = null;
        String channel = null;
        String tierCode = null;
        String originOrder = null;
        String customerIdentifier = null;
        String transactionId = null;
        LocalDateTime expirationDate = null;
        ReservationResponsePrice price = null;
        Set<ReservationResponseItem> items = null;
        ReservationResponseOrderStatus currentStatus = null;
        if ( orderEntity != null ) {
            orderId = orderEntity.getId();
            commerceOrderId = orderEntity.getCommerceOrderId();
            partnerOrderId = orderEntity.getPartnerOrderId();
            partnerCode = orderEntity.getPartnerCode();
            submittedDate = orderEntity.getSubmittedDate();
            channel = orderEntity.getChannel();
            tierCode = orderEntity.getTierCode();
            originOrder = orderEntity.getOriginOrder();
            customerIdentifier = orderEntity.getCustomerIdentifier();
            transactionId = orderEntity.getTransactionId();
            expirationDate = orderEntity.getExpirationDate();
            price = orderPriceEntityToReservationResponsePrice( orderEntity.getPrice() );
            items = orderItemEntitySetToReservationResponseItemSet( orderEntity.getItems() );
            currentStatus = orderCurrentStatusEntityToReservationResponseOrderStatus( orderEntity.getCurrentStatus() );
        }
        int expirationTimer1 = 0;
        expirationTimer1 = expirationTimer;

        ReservationResponse reservationResponse = new ReservationResponse( orderId, commerceOrderId, partnerOrderId, partnerCode, submittedDate, channel, tierCode, originOrder, customerIdentifier, transactionId, expirationDate, expirationTimer1, price, items, currentStatus );

        return reservationResponse;
    }

    protected PartnerReservationItem reservationItemToPartnerReservationItem(ReservationItem reservationItem) {
        if ( reservationItem == null ) {
            return null;
        }

        PartnerReservationItem.PartnerReservationItemBuilder partnerReservationItem = PartnerReservationItem.builder();

        partnerReservationItem.productType( reservationItem.getProductType() );

        return partnerReservationItem.build();
    }

    protected List<PartnerReservationItem> reservationItemListToPartnerReservationItemList(List<ReservationItem> list) {
        if ( list == null ) {
            return null;
        }

        List<PartnerReservationItem> list1 = new ArrayList<PartnerReservationItem>( list.size() );
        for ( ReservationItem reservationItem : list ) {
            list1.add( reservationItemToPartnerReservationItem( reservationItem ) );
        }

        return list1;
    }

    protected List<PartnerReservationDocument> reservationDocumentListToPartnerReservationDocumentList(List<ReservationDocument> list) {
        if ( list == null ) {
            return null;
        }

        List<PartnerReservationDocument> list1 = new ArrayList<PartnerReservationDocument>( list.size() );
        for ( ReservationDocument reservationDocument : list ) {
            list1.add( toPartnerReservationDocument( reservationDocument ) );
        }

        return list1;
    }

    protected PartnerReservationPax reservationPaxToPartnerReservationPax(ReservationPax reservationPax) {
        if ( reservationPax == null ) {
            return null;
        }

        PartnerReservationPax.PartnerReservationPaxBuilder partnerReservationPax = PartnerReservationPax.builder();

        partnerReservationPax.type( reservationPax.getType() );
        partnerReservationPax.firstName( reservationPax.getFirstName() );
        partnerReservationPax.lastName( reservationPax.getLastName() );
        partnerReservationPax.gender( reservationPax.getGender() );
        partnerReservationPax.birthDate( reservationPax.getBirthDate() );
        partnerReservationPax.email( reservationPax.getEmail() );
        partnerReservationPax.areaCode( reservationPax.getAreaCode() );
        partnerReservationPax.phoneNumber( reservationPax.getPhoneNumber() );
        partnerReservationPax.documents( reservationDocumentListToPartnerReservationDocumentList( reservationPax.getDocuments() ) );

        return partnerReservationPax.build();
    }

    protected List<PartnerReservationPax> reservationPaxListToPartnerReservationPaxList(List<ReservationPax> list) {
        if ( list == null ) {
            return null;
        }

        List<PartnerReservationPax> list1 = new ArrayList<PartnerReservationPax>( list.size() );
        for ( ReservationPax reservationPax : list ) {
            list1.add( reservationPaxToPartnerReservationPax( reservationPax ) );
        }

        return list1;
    }

    protected ReservationResponsePriceDescription orderPriceDescriptionEntityToReservationResponsePriceDescription(OrderPriceDescriptionEntity orderPriceDescriptionEntity) {
        if ( orderPriceDescriptionEntity == null ) {
            return null;
        }

        BigDecimal amount = null;
        BigDecimal pointsAmount = null;
        String type = null;
        String description = null;

        amount = orderPriceDescriptionEntity.getAmount();
        pointsAmount = orderPriceDescriptionEntity.getPointsAmount();
        type = orderPriceDescriptionEntity.getType();
        description = orderPriceDescriptionEntity.getDescription();

        ReservationResponsePriceDescription reservationResponsePriceDescription = new ReservationResponsePriceDescription( amount, pointsAmount, type, description );

        return reservationResponsePriceDescription;
    }

    protected Set<ReservationResponsePriceDescription> orderPriceDescriptionEntitySetToReservationResponsePriceDescriptionSet(Set<OrderPriceDescriptionEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponsePriceDescription> set1 = new LinkedHashSet<ReservationResponsePriceDescription>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( OrderPriceDescriptionEntity orderPriceDescriptionEntity : set ) {
            set1.add( orderPriceDescriptionEntityToReservationResponsePriceDescription( orderPriceDescriptionEntity ) );
        }

        return set1;
    }

    protected ReservationResponsePrice orderPriceEntityToReservationResponsePrice(OrderPriceEntity orderPriceEntity) {
        if ( orderPriceEntity == null ) {
            return null;
        }

        Double accrualPoints = null;
        BigDecimal amount = null;
        BigDecimal pointsAmount = null;
        BigDecimal partnerAmount = null;
        String priceListId = null;
        Set<ReservationResponsePriceDescription> ordersPriceDescription = null;

        if ( orderPriceEntity.getAccrualPoints() != null ) {
            accrualPoints = orderPriceEntity.getAccrualPoints().doubleValue();
        }
        amount = orderPriceEntity.getAmount();
        pointsAmount = orderPriceEntity.getPointsAmount();
        partnerAmount = orderPriceEntity.getPartnerAmount();
        priceListId = orderPriceEntity.getPriceListId();
        ordersPriceDescription = orderPriceDescriptionEntitySetToReservationResponsePriceDescriptionSet( orderPriceEntity.getOrdersPriceDescription() );

        ReservationResponsePrice reservationResponsePrice = new ReservationResponsePrice( accrualPoints, amount, pointsAmount, partnerAmount, priceListId, ordersPriceDescription );

        return reservationResponsePrice;
    }

    protected ReservationResponsePriceModality priceModalityEntityToReservationResponsePriceModality(PriceModalityEntity priceModalityEntity) {
        if ( priceModalityEntity == null ) {
            return null;
        }

        Long id = null;
        BigDecimal amount = null;
        BigDecimal pointsAmount = null;
        Float multiplier = null;
        Float multiplierAccrual = null;
        Float markup = null;
        Double accrualPoints = null;
        String priceListId = null;

        id = priceModalityEntity.getId();
        amount = priceModalityEntity.getAmount();
        pointsAmount = priceModalityEntity.getPointsAmount();
        multiplier = priceModalityEntity.getMultiplier();
        multiplierAccrual = priceModalityEntity.getMultiplierAccrual();
        markup = priceModalityEntity.getMarkup();
        accrualPoints = priceModalityEntity.getAccrualPoints();
        priceListId = priceModalityEntity.getPriceListId();

        ReservationResponsePriceModality reservationResponsePriceModality = new ReservationResponsePriceModality( id, amount, pointsAmount, multiplier, multiplierAccrual, markup, accrualPoints, priceListId );

        return reservationResponsePriceModality;
    }

    protected Set<ReservationResponsePriceModality> priceModalityEntitySetToReservationResponsePriceModalitySet(Set<PriceModalityEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponsePriceModality> set1 = new LinkedHashSet<ReservationResponsePriceModality>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PriceModalityEntity priceModalityEntity : set ) {
            set1.add( priceModalityEntityToReservationResponsePriceModality( priceModalityEntity ) );
        }

        return set1;
    }

    protected ReservationResponseOrderItemPrice orderItemPriceEntityToReservationResponseOrderItemPrice(OrderItemPriceEntity orderItemPriceEntity) {
        if ( orderItemPriceEntity == null ) {
            return null;
        }

        String listPrice = null;
        BigDecimal amount = null;
        BigDecimal pointsAmount = null;
        BigDecimal accrualPoints = null;
        BigDecimal partnerAmount = null;
        Float multiplier = null;
        Float multiplierAccrual = null;
        Float markup = null;
        String priceListId = null;
        String priceRule = null;
        Set<ReservationResponsePriceModality> pricesModalities = null;

        listPrice = orderItemPriceEntity.getListPrice();
        amount = orderItemPriceEntity.getAmount();
        pointsAmount = orderItemPriceEntity.getPointsAmount();
        accrualPoints = orderItemPriceEntity.getAccrualPoints();
        partnerAmount = orderItemPriceEntity.getPartnerAmount();
        multiplier = orderItemPriceEntity.getMultiplier();
        multiplierAccrual = orderItemPriceEntity.getMultiplierAccrual();
        markup = orderItemPriceEntity.getMarkup();
        priceListId = orderItemPriceEntity.getPriceListId();
        priceRule = orderItemPriceEntity.getPriceRule();
        pricesModalities = priceModalityEntitySetToReservationResponsePriceModalitySet( orderItemPriceEntity.getPricesModalities() );

        ReservationResponseOrderItemPrice reservationResponseOrderItemPrice = new ReservationResponseOrderItemPrice( listPrice, amount, pointsAmount, accrualPoints, partnerAmount, multiplier, multiplierAccrual, markup, priceListId, priceRule, pricesModalities );

        return reservationResponseOrderItemPrice;
    }

    protected ReservationResponseDocument documentEntityToReservationResponseDocument(DocumentEntity documentEntity) {
        if ( documentEntity == null ) {
            return null;
        }

        String documentNumber = null;
        String type = null;
        String issueDate = null;
        String issuingCountry = null;
        String expirationDate = null;
        String residenceCountry = null;

        documentNumber = documentEntity.getDocumentNumber();
        type = documentEntity.getType();
        issueDate = documentEntity.getIssueDate();
        issuingCountry = documentEntity.getIssuingCountry();
        expirationDate = documentEntity.getExpirationDate();
        residenceCountry = documentEntity.getResidenceCountry();

        ReservationResponseDocument reservationResponseDocument = new ReservationResponseDocument( documentNumber, type, issueDate, issuingCountry, expirationDate, residenceCountry );

        return reservationResponseDocument;
    }

    protected Set<ReservationResponseDocument> documentEntitySetToReservationResponseDocumentSet(Set<DocumentEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseDocument> set1 = new LinkedHashSet<ReservationResponseDocument>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( DocumentEntity documentEntity : set ) {
            set1.add( documentEntityToReservationResponseDocument( documentEntity ) );
        }

        return set1;
    }

    protected ReservationResponsePax paxEntityToReservationResponsePax(PaxEntity paxEntity) {
        if ( paxEntity == null ) {
            return null;
        }

        String type = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String areaCode = null;
        String phoneNumber = null;
        String gender = null;
        String birthDate = null;
        Set<ReservationResponseDocument> documents = null;

        type = paxEntity.getType();
        firstName = paxEntity.getFirstName();
        lastName = paxEntity.getLastName();
        email = paxEntity.getEmail();
        areaCode = paxEntity.getAreaCode();
        phoneNumber = paxEntity.getPhoneNumber();
        gender = paxEntity.getGender();
        birthDate = paxEntity.getBirthDate();
        documents = documentEntitySetToReservationResponseDocumentSet( paxEntity.getDocuments() );

        ReservationResponsePax reservationResponsePax = new ReservationResponsePax( type, firstName, lastName, email, areaCode, phoneNumber, gender, birthDate, documents );

        return reservationResponsePax;
    }

    protected Set<ReservationResponsePax> paxEntitySetToReservationResponsePaxSet(Set<PaxEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponsePax> set1 = new LinkedHashSet<ReservationResponsePax>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( PaxEntity paxEntity : set ) {
            set1.add( paxEntityToReservationResponsePax( paxEntity ) );
        }

        return set1;
    }

    protected ReservationResponseTravelInfo travelInfoEntityToReservationResponseTravelInfo(TravelInfoEntity travelInfoEntity) {
        if ( travelInfoEntity == null ) {
            return null;
        }

        String type = null;
        String reservationCode = null;
        Integer adt = null;
        Integer chd = null;
        Integer inf = null;
        String voucher = null;
        String cabinClass = null;
        Set<ReservationResponsePax> paxs = null;

        type = travelInfoEntity.getType();
        reservationCode = travelInfoEntity.getReservationCode();
        adt = travelInfoEntity.getAdt();
        chd = travelInfoEntity.getChd();
        inf = travelInfoEntity.getInf();
        voucher = travelInfoEntity.getVoucher();
        cabinClass = travelInfoEntity.getCabinClass();
        paxs = paxEntitySetToReservationResponsePaxSet( travelInfoEntity.getPaxs() );

        ReservationResponseTravelInfo reservationResponseTravelInfo = new ReservationResponseTravelInfo( type, reservationCode, adt, chd, inf, voucher, cabinClass, paxs );

        return reservationResponseTravelInfo;
    }

    protected ReservationResponseLuggage luggageEntityToReservationResponseLuggage(LuggageEntity luggageEntity) {
        if ( luggageEntity == null ) {
            return null;
        }

        String description = null;
        String type = null;

        description = luggageEntity.getDescription();
        type = luggageEntity.getType();

        ReservationResponseLuggage reservationResponseLuggage = new ReservationResponseLuggage( description, type );

        return reservationResponseLuggage;
    }

    protected Set<ReservationResponseLuggage> luggageEntitySetToReservationResponseLuggageSet(Set<LuggageEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseLuggage> set1 = new LinkedHashSet<ReservationResponseLuggage>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( LuggageEntity luggageEntity : set ) {
            set1.add( luggageEntityToReservationResponseLuggage( luggageEntity ) );
        }

        return set1;
    }

    protected ReservationResponseCancellationRule cancellationRuleEntityToReservationResponseCancellationRule(CancellationRuleEntity cancellationRuleEntity) {
        if ( cancellationRuleEntity == null ) {
            return null;
        }

        String description = null;
        String type = null;

        description = cancellationRuleEntity.getDescription();
        type = cancellationRuleEntity.getType();

        ReservationResponseCancellationRule reservationResponseCancellationRule = new ReservationResponseCancellationRule( description, type );

        return reservationResponseCancellationRule;
    }

    protected Set<ReservationResponseCancellationRule> cancellationRuleEntitySetToReservationResponseCancellationRuleSet(Set<CancellationRuleEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseCancellationRule> set1 = new LinkedHashSet<ReservationResponseCancellationRule>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( CancellationRuleEntity cancellationRuleEntity : set ) {
            set1.add( cancellationRuleEntityToReservationResponseCancellationRule( cancellationRuleEntity ) );
        }

        return set1;
    }

    protected ReservationResponseChangeRule changeRuleEntityToReservationResponseChangeRule(ChangeRuleEntity changeRuleEntity) {
        if ( changeRuleEntity == null ) {
            return null;
        }

        String description = null;
        String type = null;

        description = changeRuleEntity.getDescription();
        type = changeRuleEntity.getType();

        ReservationResponseChangeRule reservationResponseChangeRule = new ReservationResponseChangeRule( description, type );

        return reservationResponseChangeRule;
    }

    protected Set<ReservationResponseChangeRule> changeRuleEntitySetToReservationResponseChangeRuleSet(Set<ChangeRuleEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseChangeRule> set1 = new LinkedHashSet<ReservationResponseChangeRule>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ChangeRuleEntity changeRuleEntity : set ) {
            set1.add( changeRuleEntityToReservationResponseChangeRule( changeRuleEntity ) );
        }

        return set1;
    }

    protected ReservationResponseFlightLeg flightLegEntityToReservationResponseFlightLeg(FlightLegEntity flightLegEntity) {
        if ( flightLegEntity == null ) {
            return null;
        }

        String flightNumber = null;
        Integer flightDuration = null;
        String airlineManagedByIata = null;
        String airlineManagedByDescription = null;
        String airlineOperatedByIata = null;
        String airlineOperatedByDescription = null;
        Integer timeToWait = null;
        String originIata = null;
        String originCity = null;
        String originAirport = null;
        String destinationIata = null;
        String destinationCity = null;
        String destinationAirport = null;
        LocalDateTime departureDate = null;
        LocalDateTime arrivalDate = null;
        String type = null;

        flightNumber = flightLegEntity.getFlightNumber();
        flightDuration = flightLegEntity.getFlightDuration();
        airlineManagedByIata = flightLegEntity.getAirlineManagedByIata();
        airlineManagedByDescription = flightLegEntity.getAirlineManagedByDescription();
        airlineOperatedByIata = flightLegEntity.getAirlineOperatedByIata();
        airlineOperatedByDescription = flightLegEntity.getAirlineOperatedByDescription();
        timeToWait = flightLegEntity.getTimeToWait();
        originIata = flightLegEntity.getOriginIata();
        originCity = flightLegEntity.getOriginCity();
        originAirport = flightLegEntity.getOriginAirport();
        destinationIata = flightLegEntity.getDestinationIata();
        destinationCity = flightLegEntity.getDestinationCity();
        destinationAirport = flightLegEntity.getDestinationAirport();
        departureDate = flightLegEntity.getDepartureDate();
        arrivalDate = flightLegEntity.getArrivalDate();
        type = flightLegEntity.getType();

        ReservationResponseFlightLeg reservationResponseFlightLeg = new ReservationResponseFlightLeg( flightNumber, flightDuration, airlineManagedByIata, airlineManagedByDescription, airlineOperatedByIata, airlineOperatedByDescription, timeToWait, originIata, originCity, originAirport, destinationIata, destinationCity, destinationAirport, departureDate, arrivalDate, type );

        return reservationResponseFlightLeg;
    }

    protected Set<ReservationResponseFlightLeg> flightLegEntitySetToReservationResponseFlightLegSet(Set<FlightLegEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseFlightLeg> set1 = new LinkedHashSet<ReservationResponseFlightLeg>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( FlightLegEntity flightLegEntity : set ) {
            set1.add( flightLegEntityToReservationResponseFlightLeg( flightLegEntity ) );
        }

        return set1;
    }

    protected ReservationResponseSegment segmentEntityToReservationResponseSegment(SegmentEntity segmentEntity) {
        if ( segmentEntity == null ) {
            return null;
        }

        String partnerId = null;
        String step = null;
        Integer stops = null;
        Integer flightDuration = null;
        String originIata = null;
        String originCity = null;
        String originAirport = null;
        String destinationIata = null;
        String destinationCity = null;
        String destinationAirport = null;
        LocalDateTime departureDate = null;
        LocalDateTime arrivalDate = null;
        Set<ReservationResponseLuggage> luggages = null;
        Set<ReservationResponseCancellationRule> cancellationRules = null;
        Set<ReservationResponseChangeRule> changeRules = null;
        Set<ReservationResponseFlightLeg> flightsLegs = null;

        partnerId = segmentEntity.getPartnerId();
        step = segmentEntity.getStep();
        stops = segmentEntity.getStops();
        flightDuration = segmentEntity.getFlightDuration();
        originIata = segmentEntity.getOriginIata();
        originCity = segmentEntity.getOriginCity();
        originAirport = segmentEntity.getOriginAirport();
        destinationIata = segmentEntity.getDestinationIata();
        destinationCity = segmentEntity.getDestinationCity();
        destinationAirport = segmentEntity.getDestinationAirport();
        departureDate = segmentEntity.getDepartureDate();
        arrivalDate = segmentEntity.getArrivalDate();
        luggages = luggageEntitySetToReservationResponseLuggageSet( segmentEntity.getLuggages() );
        cancellationRules = cancellationRuleEntitySetToReservationResponseCancellationRuleSet( segmentEntity.getCancellationRules() );
        changeRules = changeRuleEntitySetToReservationResponseChangeRuleSet( segmentEntity.getChangeRules() );
        flightsLegs = flightLegEntitySetToReservationResponseFlightLegSet( segmentEntity.getFlightsLegs() );

        ReservationResponseSegment reservationResponseSegment = new ReservationResponseSegment( partnerId, step, stops, flightDuration, originIata, originCity, originAirport, destinationIata, destinationCity, destinationAirport, departureDate, arrivalDate, luggages, cancellationRules, changeRules, flightsLegs );

        return reservationResponseSegment;
    }

    protected Set<ReservationResponseSegment> segmentEntitySetToReservationResponseSegmentSet(Set<SegmentEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseSegment> set1 = new LinkedHashSet<ReservationResponseSegment>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SegmentEntity segmentEntity : set ) {
            set1.add( segmentEntityToReservationResponseSegment( segmentEntity ) );
        }

        return set1;
    }

    protected ReservationResponseItem orderItemEntityToReservationResponseItem(OrderItemEntity orderItemEntity) {
        if ( orderItemEntity == null ) {
            return null;
        }

        String commerceItemId = null;
        String skuId = null;
        String productId = null;
        Integer quantity = null;
        String externalCoupon = null;
        ReservationResponseOrderItemPrice price = null;
        ReservationResponseTravelInfo travelInfo = null;
        Set<ReservationResponseSegment> segments = null;

        commerceItemId = orderItemEntity.getCommerceItemId();
        skuId = orderItemEntity.getSkuId();
        productId = orderItemEntity.getProductId();
        quantity = orderItemEntity.getQuantity();
        externalCoupon = orderItemEntity.getExternalCoupon();
        price = orderItemPriceEntityToReservationResponseOrderItemPrice( orderItemEntity.getPrice() );
        travelInfo = travelInfoEntityToReservationResponseTravelInfo( orderItemEntity.getTravelInfo() );
        segments = segmentEntitySetToReservationResponseSegmentSet( orderItemEntity.getSegments() );

        ReservationResponseItem reservationResponseItem = new ReservationResponseItem( commerceItemId, skuId, productId, quantity, externalCoupon, price, travelInfo, segments );

        return reservationResponseItem;
    }

    protected Set<ReservationResponseItem> orderItemEntitySetToReservationResponseItemSet(Set<OrderItemEntity> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReservationResponseItem> set1 = new LinkedHashSet<ReservationResponseItem>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( OrderItemEntity orderItemEntity : set ) {
            set1.add( orderItemEntityToReservationResponseItem( orderItemEntity ) );
        }

        return set1;
    }

    protected ReservationResponseOrderStatus orderCurrentStatusEntityToReservationResponseOrderStatus(OrderCurrentStatusEntity orderCurrentStatusEntity) {
        if ( orderCurrentStatusEntity == null ) {
            return null;
        }

        String code = null;
        String description = null;
        String partnerCode = null;
        String partnerDescription = null;
        String partnerResponse = null;

        code = orderCurrentStatusEntity.getCode();
        description = orderCurrentStatusEntity.getDescription();
        partnerCode = orderCurrentStatusEntity.getPartnerCode();
        partnerDescription = orderCurrentStatusEntity.getPartnerDescription();
        partnerResponse = orderCurrentStatusEntity.getPartnerResponse();

        LocalDateTime statusDate = null;

        ReservationResponseOrderStatus reservationResponseOrderStatus = new ReservationResponseOrderStatus( code, description, partnerCode, partnerDescription, partnerResponse, statusDate );

        return reservationResponseOrderStatus;
    }
}
