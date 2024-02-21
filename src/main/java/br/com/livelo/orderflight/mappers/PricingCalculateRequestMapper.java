package br.com.livelo.orderflight.mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateAirline;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateCancelationRule;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateChangeRule;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateFlightsLeg;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateItem;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateLuggage;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateManagedBy;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateOperatedBy;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateRequest;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateSegment;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateTaxes;
import br.com.livelo.orderflight.domain.dtos.pricing.request.PricingCalculateTravelInfo;
import br.com.livelo.orderflight.exception.OrderFlightException;
import lombok.experimental.UtilityClass;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;


@UtilityClass
public class PricingCalculateRequestMapper {
	private final String TYPE_FLIGHT = "type_flight";

	private static BigDecimal totalTaxes = new BigDecimal(0);
	private static BigDecimal totalflight = new BigDecimal(0);
	public static PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse) {
		var partnerReservationItemTypeFlight = getItemTypeFlight(partnerReservationResponse);
		return PricingCalculateRequest.builder()
				.travelInfo(buildTravelInfo(partnerReservationItemTypeFlight))
				.items(buildListPricingCalculateItens(partnerReservationResponse,partnerReservationItemTypeFlight))
				.build();
	}

	private static PricingCalculateTravelInfo buildTravelInfo(PartnerReservationItem partnerReservationItemTypeFlight) {
		return PricingCalculateTravelInfo.builder()
				.type(partnerReservationItemTypeFlight.getTravelInfo().getType())
				.adultQuantity(partnerReservationItemTypeFlight.getTravelInfo().getAdultQuantity())
				.childQuantity(partnerReservationItemTypeFlight.getTravelInfo().getChildQuantity())
				.babyQuantity(partnerReservationItemTypeFlight.getTravelInfo().getBabyQuantity())
				.typeClass(partnerReservationItemTypeFlight.getTravelInfo().getTypeClass())
				.stageJourney("RESERVATION")
				.build();
	}

	private static ArrayList<PricingCalculateItem> buildListPricingCalculateItens(PartnerReservationResponse partnerReservationResponse, PartnerReservationItem partnerReservationItemTypeFlight) {
		var pricingCalculateItem = PricingCalculateItem.builder()
				.id(partnerReservationResponse.getCommerceOrderId())
				.flightType(partnerReservationItemTypeFlight.getTravelInfo().getType())
				.price(buildPricingCalculatePrice(partnerReservationResponse))
				.segments(buildSegments(partnerReservationItemTypeFlight))
				.build();
		return new ArrayList<>(List.of(pricingCalculateItem));
	}

	private static ArrayList<PricingCalculateSegment> buildSegments(PartnerReservationItem partnerReservationItemTypeFlight) {
		var segments = new ArrayList<PricingCalculateSegment>();
		for(PartnerReservationSegment segment:partnerReservationItemTypeFlight.getSegments()) {
			var flightsLegs = buildFlightsLegs(segment,partnerReservationItemTypeFlight);
			segments.add(PricingCalculateSegment.builder()
					.type(null)
					.step(Integer.valueOf(segment.getStep()))
					.originIata(segment.getOriginIata())
					.originDescription(segment.getOriginDescription())
					.originDate(segment.getDepartureDate())
					.destinationIata(segment.getDestinationIata())
					.destinationDescription(segment.getDestinationDescription())
					.destinationDate(segment.getArrivalDate())
					.numberOfStops(segment.getStops())
					.flightDuration(segment.getFlightDuration())
					.airline(flightsLegs.getFirst().getAirline())
					.flightClass(null)//ACHAR
					.luggages(buildLuggages(segment))
					.cancelationRules(buildCancelationRules(segment))
					.changeRules(buildChangeRules(segment))
					.flightsLegs(flightsLegs)
					.build());
		}
		return segments;
	}

	private static ArrayList<PricingCalculateFlightsLeg> buildFlightsLegs(PartnerReservationSegment segment, PartnerReservationItem partnerReservationItemTypeFlight) {
		var flightsLegs = new ArrayList<PricingCalculateFlightsLeg>();
		for(PartnerReservationFlightsLeg partnerReservationFlightsLeg: segment.getFlightsLegs()) {
			flightsLegs.add(PricingCalculateFlightsLeg.builder()
					.airline(PricingCalculateAirline.builder()
							.iata(partnerReservationFlightsLeg.getAirline())
							.description(partnerReservationFlightsLeg.getAirline())
							.managedBy(PricingCalculateManagedBy.builder()
									.iata(partnerReservationFlightsLeg.getManagedBy())
									.description(partnerReservationFlightsLeg.getManagedBy())
									.build())
							.operatedBy(PricingCalculateOperatedBy.builder()
									.iata(partnerReservationFlightsLeg.getOperatedBy())
									.description(partnerReservationFlightsLeg.getOperatedBy())
									.build())
							.build())
					.flightNumber(partnerReservationFlightsLeg.getFlightNumber())
					.flightDuration(partnerReservationFlightsLeg.getFlightDuration())
					.originIata(partnerReservationFlightsLeg.getOriginIata())
					.timeToWait(partnerReservationFlightsLeg.getTimeToWait())
					.originDescription(partnerReservationFlightsLeg.getOriginDescription())
					.departureDate(partnerReservationFlightsLeg.getDepartureDate())
					.destinationIata(partnerReservationFlightsLeg.getDestinationIata())
					.destinationDescription(partnerReservationFlightsLeg.getDestinationDescription())
					.arrivalDate(partnerReservationFlightsLeg.getArrivalDate())
					.flightClass(partnerReservationItemTypeFlight.getTravelInfo().getTypeClass())
					.build());
		}
		return flightsLegs;
	}

	private static ArrayList<PricingCalculateChangeRule> buildChangeRules(PartnerReservationSegment segment) {
		var changeRules = new ArrayList<PricingCalculateChangeRule>();
		for(PartnerReservationChangeRule partnerReservationChangeRule : segment.getChangeRules()) {
			changeRules.add(PricingCalculateChangeRule.builder().description(partnerReservationChangeRule.getDescription()).build());
		}
		return changeRules;
	}

	private static ArrayList<PricingCalculateCancelationRule> buildCancelationRules(PartnerReservationSegment segment) {
		var cancelationRules = new ArrayList<PricingCalculateCancelationRule>();
		for(PartnerReservationCancelationRule partnerReservationCancelationRule : segment.getCancelationRules()) {
			cancelationRules.add(PricingCalculateCancelationRule.builder().description(partnerReservationCancelationRule.getDescription()).build());
		}
		return cancelationRules;
	}

	private static ArrayList<PricingCalculateLuggage> buildLuggages(PartnerReservationSegment segment) {
		var luggages = new ArrayList<PricingCalculateLuggage>();
		for(PartnerReservationLuggage partnerReservationLuggage:segment.getLuggages()) {
			luggages.add(PricingCalculateLuggage.builder()
					.type(partnerReservationLuggage.getType())
					.quantity(null)//ACHAR
					.weight(null)//ACHAR
					.measurement(null)//ACHAR
					.description(partnerReservationLuggage.getDescription())
					.build());
		}
		return luggages;
	}

	private static PricingCalculatePrice buildPricingCalculatePrice(PartnerReservationResponse partnerReservationResponse) {
		return PricingCalculatePrice.builder()
				.currency("BRL")
				.amount(partnerReservationResponse.getAmount())
				.flight(PricingCalculateFlight.builder().amount(totalflight).build())
				.taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
				.pricesDescription(buildPricesDescription(partnerReservationResponse.getOrdersPriceDescription()))
				.build();
	}

	private static ArrayList<PricingCalculatePricesDescription> buildPricesDescription(List<PartnerReservationOrdersPriceDescription> ordersPriceDescription) {
		var pricesDescription = new ArrayList<PricingCalculatePricesDescription>();
		for(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription : ordersPriceDescription) {
			BigDecimal taxes = new BigDecimal(0);
			var pricingCalculateTaxes = new ArrayList<PricingCalculateTaxes>();
			for(PartnerReservationOrdersPriceDescriptionTaxes partnerReservationOrdersPriceDescriptionTaxes:partnerReservationOrdersPriceDescription.getTaxes()) {
				taxes = taxes.add(partnerReservationOrdersPriceDescriptionTaxes.getAmount());
				pricingCalculateTaxes.add(PricingCalculateTaxes.builder()
						.description(partnerReservationOrdersPriceDescriptionTaxes.getDescription())
						.amount(partnerReservationOrdersPriceDescriptionTaxes.getAmount())
						.build());
			}
			pricesDescription.add(PricingCalculatePricesDescription.builder()
					.amount(partnerReservationOrdersPriceDescription.getAmount().add(taxes))
					.flight(PricingCalculateFlight.builder().amount(partnerReservationOrdersPriceDescription.getAmount()).build())
					.passengerType(partnerReservationOrdersPriceDescription.getType())
					.passengerCount(null)
					.taxes(pricingCalculateTaxes)
					.build());
			totalTaxes = totalTaxes.add(taxes);
			totalflight = totalflight.add(partnerReservationOrdersPriceDescription.getAmount());
		}
		return pricesDescription;
	}

	private static PartnerReservationItem getItemTypeFlight(PartnerReservationResponse partnerReservationResponse) {
		return partnerReservationResponse.getItems()
				.stream()
				.filter(item -> TYPE_FLIGHT.equals(item.getType()))
				.findFirst()
				.orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Type Flight not found"));
	}
}
