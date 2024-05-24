package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationCancellationRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationChangeRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightLegAirline;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationLuggage;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateAirline;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateCancellationRule;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateChangeRule;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateFlightsLeg;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateLuggage;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateManagedBy;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateOperatedBy;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateSegment;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateTravelInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PricingCalculateRequestMapperImpl implements PricingCalculateRequestMapper {

    @Override
    public PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse, String commerceOrderId) {
        if ( partnerReservationResponse == null && commerceOrderId == null ) {
            return null;
        }

        PricingCalculateRequest.PricingCalculateRequestBuilder pricingCalculateRequest = PricingCalculateRequest.builder();

        pricingCalculateRequest.travelInfo( buildTravelInfo(getItemTypeFlight(partnerReservationResponse)) );
        pricingCalculateRequest.items( buildListPricingCalculateItems(partnerReservationResponse, getItemTypeFlight(partnerReservationResponse), commerceOrderId) );

        return pricingCalculateRequest.build();
    }

    @Override
    public PricingCalculateTravelInfo buildTravelInfo(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }

        PricingCalculateTravelInfo.PricingCalculateTravelInfoBuilder pricingCalculateTravelInfo = PricingCalculateTravelInfo.builder();

        pricingCalculateTravelInfo.type( partnerReservationItemTravelInfoType( partnerReservationItem ) );
        pricingCalculateTravelInfo.adt( partnerReservationItemTravelInfoAdt( partnerReservationItem ) );
        pricingCalculateTravelInfo.chd( partnerReservationItemTravelInfoChd( partnerReservationItem ) );
        pricingCalculateTravelInfo.inf( partnerReservationItemTravelInfoInf( partnerReservationItem ) );
        pricingCalculateTravelInfo.cabinClass( partnerReservationItemTravelInfoCabinClass( partnerReservationItem ) );
        Boolean isInternational = partnerReservationItemTravelInfoIsInternational( partnerReservationItem );
        if ( isInternational != null ) {
            pricingCalculateTravelInfo.isInternational( isInternational );
        }
        else {
            pricingCalculateTravelInfo.isInternational( false );
        }

        pricingCalculateTravelInfo.stageJourney( "RESERVATION" );

        return pricingCalculateTravelInfo.build();
    }

    @Override
    public PricingCalculatePrice toPricingCalculatePrice(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }

        PricingCalculatePrice.PricingCalculatePriceBuilder pricingCalculatePrice = PricingCalculatePrice.builder();

        pricingCalculatePrice.amount( partnerReservationResponse.getAmount() );
        pricingCalculatePrice.pricesDescription( partnerReservationOrdersPriceDescriptionToPricingCalculatePricesDescription( partnerReservationResponse.getOrdersPriceDescription() ) );
        pricingCalculatePrice.flight( partnerReservationResponseToPricingCalculateFlight( partnerReservationResponse ) );
        pricingCalculatePrice.taxes( partnerReservationResponseToPricingCalculateTaxes( partnerReservationResponse ) );

        pricingCalculatePrice.currency( "BRL" );

        return pricingCalculatePrice.build();
    }

    @Override
    public PricingCalculateSegment toPricingCalculateSegment(PartnerReservationSegment partnerReservationSegment) {
        if ( partnerReservationSegment == null ) {
            return null;
        }

        PricingCalculateSegment.PricingCalculateSegmentBuilder pricingCalculateSegment = PricingCalculateSegment.builder();

        if ( partnerReservationSegment.getStep() != null ) {
            pricingCalculateSegment.step( Integer.parseInt( partnerReservationSegment.getStep() ) );
        }
        pricingCalculateSegment.originIata( partnerReservationSegment.getOriginIata() );
        pricingCalculateSegment.originCity( partnerReservationSegment.getOriginCity() );
        pricingCalculateSegment.originAirport( partnerReservationSegment.getOriginAirport() );
        pricingCalculateSegment.departureDate( partnerReservationSegment.getDepartureDate() );
        pricingCalculateSegment.destinationIata( partnerReservationSegment.getDestinationIata() );
        pricingCalculateSegment.destinationCity( partnerReservationSegment.getDestinationCity() );
        pricingCalculateSegment.destinationAirport( partnerReservationSegment.getDestinationAirport() );
        pricingCalculateSegment.arrivalDate( partnerReservationSegment.getArrivalDate() );
        if ( partnerReservationSegment.getStops() != null ) {
            pricingCalculateSegment.stops( partnerReservationSegment.getStops() );
        }
        if ( partnerReservationSegment.getFlightDuration() != null ) {
            pricingCalculateSegment.flightDuration( partnerReservationSegment.getFlightDuration() );
        }
        pricingCalculateSegment.airline( partnerReservationAirlineToPricingCalculateAirline( partnerReservationSegment.getAirline() ) );
        pricingCalculateSegment.cabinClass( partnerReservationSegment.getCabinClass() );

        pricingCalculateSegment.luggages( buildLuggages(partnerReservationSegment) );
        pricingCalculateSegment.cancellationRules( buildCancellationRules(partnerReservationSegment) );
        pricingCalculateSegment.changeRules( buildChangeRules(partnerReservationSegment) );

        return pricingCalculateSegment.build();
    }

    @Override
    public PricingCalculateLuggage toPricingCalculateLuggage(PartnerReservationLuggage partnerReservationLuggage) {
        if ( partnerReservationLuggage == null ) {
            return null;
        }

        PricingCalculateLuggage.PricingCalculateLuggageBuilder pricingCalculateLuggage = PricingCalculateLuggage.builder();

        pricingCalculateLuggage.type( partnerReservationLuggage.getType() );
        pricingCalculateLuggage.quantity( partnerReservationLuggage.getQuantity() );
        pricingCalculateLuggage.weight( partnerReservationLuggage.getWeight() );
        pricingCalculateLuggage.measurement( partnerReservationLuggage.getMeasurement() );
        pricingCalculateLuggage.description( partnerReservationLuggage.getDescription() );

        return pricingCalculateLuggage.build();
    }

    @Override
    public List<PricingCalculateLuggage> toPricingCalculateLuggageList(List<PartnerReservationLuggage> partnerReservationLuggages) {
        if ( partnerReservationLuggages == null ) {
            return null;
        }

        List<PricingCalculateLuggage> list = new ArrayList<PricingCalculateLuggage>( partnerReservationLuggages.size() );
        for ( PartnerReservationLuggage partnerReservationLuggage : partnerReservationLuggages ) {
            list.add( toPricingCalculateLuggage( partnerReservationLuggage ) );
        }

        return list;
    }

    @Override
    public PricingCalculateCancellationRule toPricingCalculateCancellationRule(PartnerReservationCancellationRule partnerReservationCancellationRule) {
        if ( partnerReservationCancellationRule == null ) {
            return null;
        }

        PricingCalculateCancellationRule.PricingCalculateCancellationRuleBuilder pricingCalculateCancellationRule = PricingCalculateCancellationRule.builder();

        pricingCalculateCancellationRule.description( partnerReservationCancellationRule.getDescription() );

        return pricingCalculateCancellationRule.build();
    }

    @Override
    public List<PricingCalculateCancellationRule> toPricingCalculateCancellationRuleList(List<PartnerReservationCancellationRule> partnerReservationCancelationRules) {
        if ( partnerReservationCancelationRules == null ) {
            return null;
        }

        List<PricingCalculateCancellationRule> list = new ArrayList<PricingCalculateCancellationRule>( partnerReservationCancelationRules.size() );
        for ( PartnerReservationCancellationRule partnerReservationCancellationRule : partnerReservationCancelationRules ) {
            list.add( toPricingCalculateCancellationRule( partnerReservationCancellationRule ) );
        }

        return list;
    }

    @Override
    public PricingCalculateChangeRule toPricingCalculateChangeRule(PartnerReservationChangeRule partnerReservationChangeRule) {
        if ( partnerReservationChangeRule == null ) {
            return null;
        }

        PricingCalculateChangeRule.PricingCalculateChangeRuleBuilder pricingCalculateChangeRule = PricingCalculateChangeRule.builder();

        pricingCalculateChangeRule.description( partnerReservationChangeRule.getDescription() );

        return pricingCalculateChangeRule.build();
    }

    @Override
    public List<PricingCalculateChangeRule> toPricingCalculateChangeRuleList(List<PartnerReservationChangeRule> partnerReservationChangeRules) {
        if ( partnerReservationChangeRules == null ) {
            return null;
        }

        List<PricingCalculateChangeRule> list = new ArrayList<PricingCalculateChangeRule>( partnerReservationChangeRules.size() );
        for ( PartnerReservationChangeRule partnerReservationChangeRule : partnerReservationChangeRules ) {
            list.add( toPricingCalculateChangeRule( partnerReservationChangeRule ) );
        }

        return list;
    }

    @Override
    public PricingCalculateAirline toPricingCalculateAirline(PartnerReservationFlightLegAirline partnerReservationFlightLegAirline) {
        if ( partnerReservationFlightLegAirline == null ) {
            return null;
        }

        PricingCalculateAirline.PricingCalculateAirlineBuilder pricingCalculateAirline = PricingCalculateAirline.builder();

        pricingCalculateAirline.iata( partnerReservationFlightLegAirlineManagedByIata( partnerReservationFlightLegAirline ) );
        pricingCalculateAirline.description( partnerReservationFlightLegAirlineManagedByDescription( partnerReservationFlightLegAirline ) );

        pricingCalculateAirline.managedBy( toPricingCalculateManagedBy(partnerReservationFlightLegAirline.getManagedBy()) );
        pricingCalculateAirline.operatedBy( toPricingCalculateOperatedBy(partnerReservationFlightLegAirline.getOperatedBy()) );

        return pricingCalculateAirline.build();
    }

    @Override
    public PricingCalculateManagedBy toPricingCalculateManagedBy(PartnerReservationAirline managedBy) {
        if ( managedBy == null ) {
            return null;
        }

        PricingCalculateManagedBy.PricingCalculateManagedByBuilder pricingCalculateManagedBy = PricingCalculateManagedBy.builder();

        pricingCalculateManagedBy.iata( managedBy.getIata() );
        pricingCalculateManagedBy.description( managedBy.getDescription() );

        return pricingCalculateManagedBy.build();
    }

    @Override
    public PricingCalculateOperatedBy toPricingCalculateOperatedBy(PartnerReservationAirline operatedBy) {
        if ( operatedBy == null ) {
            return null;
        }

        PricingCalculateOperatedBy.PricingCalculateOperatedByBuilder pricingCalculateOperatedBy = PricingCalculateOperatedBy.builder();

        pricingCalculateOperatedBy.iata( operatedBy.getIata() );
        pricingCalculateOperatedBy.description( operatedBy.getDescription() );

        return pricingCalculateOperatedBy.build();
    }

    @Override
    public PricingCalculateFlightsLeg toPricingCalculateFlightsLeg(PartnerReservationFlightsLeg partnerReservationFlightLeg) {
        if ( partnerReservationFlightLeg == null ) {
            return null;
        }

        PricingCalculateFlightsLeg.PricingCalculateFlightsLegBuilder pricingCalculateFlightsLeg = PricingCalculateFlightsLeg.builder();

        pricingCalculateFlightsLeg.flightNumber( partnerReservationFlightLeg.getFlightNumber() );
        pricingCalculateFlightsLeg.flightDuration( partnerReservationFlightLeg.getFlightDuration() );
        pricingCalculateFlightsLeg.originIata( partnerReservationFlightLeg.getOriginIata() );
        pricingCalculateFlightsLeg.originCity( partnerReservationFlightLeg.getOriginCity() );
        pricingCalculateFlightsLeg.originAirport( partnerReservationFlightLeg.getOriginAirport() );
        pricingCalculateFlightsLeg.timeToWait( partnerReservationFlightLeg.getTimeToWait() );
        pricingCalculateFlightsLeg.departureDate( partnerReservationFlightLeg.getDepartureDate() );
        pricingCalculateFlightsLeg.destinationIata( partnerReservationFlightLeg.getDestinationIata() );
        pricingCalculateFlightsLeg.destinationCity( partnerReservationFlightLeg.getDestinationCity() );
        pricingCalculateFlightsLeg.destinationAirport( partnerReservationFlightLeg.getDestinationAirport() );
        pricingCalculateFlightsLeg.arrivalDate( partnerReservationFlightLeg.getArrivalDate() );
        pricingCalculateFlightsLeg.cabinClass( partnerReservationFlightLeg.getCabinClass() );
        pricingCalculateFlightsLeg.fareClass( partnerReservationFlightLeg.getFareClass() );

        pricingCalculateFlightsLeg.airline( toPricingCalculateAirline(partnerReservationFlightLeg.getAirline()) );

        return pricingCalculateFlightsLeg.build();
    }

    private String partnerReservationItemTravelInfoType(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        String type = travelInfo.getType();
        if ( type == null ) {
            return null;
        }
        return type;
    }

    private Integer partnerReservationItemTravelInfoAdt(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        Integer adt = travelInfo.getAdt();
        if ( adt == null ) {
            return null;
        }
        return adt;
    }

    private Integer partnerReservationItemTravelInfoChd(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        Integer chd = travelInfo.getChd();
        if ( chd == null ) {
            return null;
        }
        return chd;
    }

    private Integer partnerReservationItemTravelInfoInf(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        Integer inf = travelInfo.getInf();
        if ( inf == null ) {
            return null;
        }
        return inf;
    }

    private String partnerReservationItemTravelInfoCabinClass(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        String cabinClass = travelInfo.getCabinClass();
        if ( cabinClass == null ) {
            return null;
        }
        return cabinClass;
    }

    private Boolean partnerReservationItemTravelInfoIsInternational(PartnerReservationItem partnerReservationItem) {
        if ( partnerReservationItem == null ) {
            return null;
        }
        PartnerReservationTravelInfo travelInfo = partnerReservationItem.getTravelInfo();
        if ( travelInfo == null ) {
            return null;
        }
        Boolean isInternational = travelInfo.getIsInternational();
        if ( isInternational == null ) {
            return null;
        }
        return isInternational;
    }

    protected PricingCalculateFlight partnerReservationOrdersPriceDescriptionFlightToPricingCalculateFlight(PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight) {
        if ( partnerReservationOrdersPriceDescriptionFlight == null ) {
            return null;
        }

        PricingCalculateFlight.PricingCalculateFlightBuilder pricingCalculateFlight = PricingCalculateFlight.builder();

        pricingCalculateFlight.amount( partnerReservationOrdersPriceDescriptionFlight.getAmount() );
        pricingCalculateFlight.passengerType( partnerReservationOrdersPriceDescriptionFlight.getPassengerType() );
        pricingCalculateFlight.passengerCount( partnerReservationOrdersPriceDescriptionFlight.getPassengerCount() );

        return pricingCalculateFlight.build();
    }

    protected List<PricingCalculateFlight> partnerReservationOrdersPriceDescriptionFlightListToPricingCalculateFlightList(List<PartnerReservationOrdersPriceDescriptionFlight> list) {
        if ( list == null ) {
            return null;
        }

        List<PricingCalculateFlight> list1 = new ArrayList<PricingCalculateFlight>( list.size() );
        for ( PartnerReservationOrdersPriceDescriptionFlight partnerReservationOrdersPriceDescriptionFlight : list ) {
            list1.add( partnerReservationOrdersPriceDescriptionFlightToPricingCalculateFlight( partnerReservationOrdersPriceDescriptionFlight ) );
        }

        return list1;
    }

    protected PricingCalculateTaxes partnerReservationOrdersPriceDescriptionTaxesToPricingCalculateTaxes(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes) {
        if ( partnerReservationOrdersPriceDescriptionTaxes == null ) {
            return null;
        }

        PricingCalculateTaxes.PricingCalculateTaxesBuilder pricingCalculateTaxes = PricingCalculateTaxes.builder();

        pricingCalculateTaxes.amount( partnerReservationOrdersPriceDescriptionTaxes.getAmount() );
        pricingCalculateTaxes.type( partnerReservationOrdersPriceDescriptionTaxes.getType() );

        return pricingCalculateTaxes.build();
    }

    protected List<PricingCalculateTaxes> partnerReservationOrdersPriceDescriptionTaxesListToPricingCalculateTaxesList(List<PartnerReservationOrdersPriceDescriptionTaxes> list) {
        if ( list == null ) {
            return null;
        }

        List<PricingCalculateTaxes> list1 = new ArrayList<PricingCalculateTaxes>( list.size() );
        for ( PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes : list ) {
            list1.add( partnerReservationOrdersPriceDescriptionTaxesToPricingCalculateTaxes( partnerReservationOrdersPriceDescriptionTaxes ) );
        }

        return list1;
    }

    protected PricingCalculatePricesDescription partnerReservationOrdersPriceDescriptionToPricingCalculatePricesDescription(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription) {
        if ( partnerReservationOrdersPriceDescription == null ) {
            return null;
        }

        PricingCalculatePricesDescription.PricingCalculatePricesDescriptionBuilder pricingCalculatePricesDescription = PricingCalculatePricesDescription.builder();

        pricingCalculatePricesDescription.flights( partnerReservationOrdersPriceDescriptionFlightListToPricingCalculateFlightList( partnerReservationOrdersPriceDescription.getFlights() ) );
        pricingCalculatePricesDescription.taxes( partnerReservationOrdersPriceDescriptionTaxesListToPricingCalculateTaxesList( partnerReservationOrdersPriceDescription.getTaxes() ) );

        return pricingCalculatePricesDescription.build();
    }

    protected PricingCalculateFlight partnerReservationResponseToPricingCalculateFlight(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }

        PricingCalculateFlight.PricingCalculateFlightBuilder pricingCalculateFlight = PricingCalculateFlight.builder();

        pricingCalculateFlight.amount( getTotalFlight(partnerReservationResponse) );

        return pricingCalculateFlight.build();
    }

    protected PricingCalculateTaxes partnerReservationResponseToPricingCalculateTaxes(PartnerReservationResponse partnerReservationResponse) {
        if ( partnerReservationResponse == null ) {
            return null;
        }

        PricingCalculateTaxes.PricingCalculateTaxesBuilder pricingCalculateTaxes = PricingCalculateTaxes.builder();

        pricingCalculateTaxes.amount( getTotalTaxes(partnerReservationResponse) );

        return pricingCalculateTaxes.build();
    }

    protected PricingCalculateAirline partnerReservationAirlineToPricingCalculateAirline(PartnerReservationAirline partnerReservationAirline) {
        if ( partnerReservationAirline == null ) {
            return null;
        }

        PricingCalculateAirline.PricingCalculateAirlineBuilder pricingCalculateAirline = PricingCalculateAirline.builder();

        pricingCalculateAirline.iata( partnerReservationAirline.getIata() );
        pricingCalculateAirline.description( partnerReservationAirline.getDescription() );

        return pricingCalculateAirline.build();
    }

    private String partnerReservationFlightLegAirlineManagedByIata(PartnerReservationFlightLegAirline partnerReservationFlightLegAirline) {
        if ( partnerReservationFlightLegAirline == null ) {
            return null;
        }
        PartnerReservationAirline managedBy = partnerReservationFlightLegAirline.getManagedBy();
        if ( managedBy == null ) {
            return null;
        }
        String iata = managedBy.getIata();
        if ( iata == null ) {
            return null;
        }
        return iata;
    }

    private String partnerReservationFlightLegAirlineManagedByDescription(PartnerReservationFlightLegAirline partnerReservationFlightLegAirline) {
        if ( partnerReservationFlightLegAirline == null ) {
            return null;
        }
        PartnerReservationAirline managedBy = partnerReservationFlightLegAirline.getManagedBy();
        if ( managedBy == null ) {
            return null;
        }
        String description = managedBy.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }
}
