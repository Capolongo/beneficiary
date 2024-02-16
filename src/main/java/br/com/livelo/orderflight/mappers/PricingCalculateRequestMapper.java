package br.com.livelo.orderflight.mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationCancelationRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationChangeRule;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationFlightsLeg;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationLuggage;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationSegment;
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
import lombok.experimental.UtilityClass;

@UtilityClass
public class PricingCalculateRequestMapper {
	private final String TYPE_FLIGHT = "type_flight";
	public static PricingCalculateRequest toPricingCalculateRequest(PartnerReservationResponse partnerReservationResponse) {
		
		var partnerReservationItemTypeFlight = partnerReservationResponse.getItems()
				.stream()
				.filter(item -> TYPE_FLIGHT.equals(item.getType()))
				.findFirst()
				.orElse(null);
		
		var pricesDescription = new ArrayList<PricingCalculatePricesDescription>();
		var totalTaxes = new BigDecimal(0);
		var totalflight = new BigDecimal(0);
		for(PartnerReservationOrdersPriceDescription partnerReservationOrdersPriceDescription : partnerReservationResponse.getOrdersPriceDescription()) {
			var taxes = new BigDecimal(0);
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
		
		var pricingCalculatePrice = PricingCalculatePrice.builder()
						.currency(null)//ACHAR
						.amount(partnerReservationResponse.getAmount())
						.flight(PricingCalculateFlight.builder().amount(totalflight).build())
						.taxes(PricingCalculateTaxes.builder().amount(totalTaxes).build())
						.pricesDescription(pricesDescription)
						.build();
		
		
		
		var segments = new ArrayList<PricingCalculateSegment>();
		for(PartnerReservationSegment segment:partnerReservationItemTypeFlight.getSegments()) {
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
			var cancelationRules = new ArrayList<PricingCalculateCancelationRule>();
			for(PartnerReservationCancelationRule partnerReservationCancelationRule : segment.getCancelationRules()) {
				cancelationRules.add(PricingCalculateCancelationRule.builder().description(partnerReservationCancelationRule.getDescription()).build());
			}
			var changeRules = new ArrayList<PricingCalculateChangeRule>();
			for(PartnerReservationChangeRule partnerReservationChangeRule : segment.getChangeRules()) {
				changeRules.add(PricingCalculateChangeRule.builder().description(partnerReservationChangeRule.getDescription()).build());
			}
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
									.flightClass(null)//ACHAR
									.build());
			}
			
			segments.add(PricingCalculateSegment.builder()
					.type(null)//ACHAR
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
					.luggages(luggages)
					.cancelationRules(cancelationRules)
					.changeRules(changeRules)
					.flightsLegs(flightsLegs)
					.build());
		}
		
		var pricingCalculateItem = PricingCalculateItem.builder()
				.id(null)//ACHAR
				.flightType(null)//ACHAR
				.price(pricingCalculatePrice)
				.segments(segments)
				.build();
	
		
		var travelInfo = PricingCalculateTravelInfo.builder()
				.type(partnerReservationItemTypeFlight.getTravelInfo().getType())
				.adultQuantity(partnerReservationItemTypeFlight.getTravelInfo().getAdultQuantity())
				.childQuantity(partnerReservationItemTypeFlight.getTravelInfo().getChildQuantity())
				.babyQuantity(partnerReservationItemTypeFlight.getTravelInfo().getBabyQuantity())
				.typeClass(partnerReservationItemTypeFlight.getTravelInfo().getTypeClass())
				.stageJourney(null)//ACHAR
				.build();
		
		return PricingCalculateRequest.builder()
				.travelInfo(travelInfo)
				.items(new ArrayList<>(List.of(pricingCalculateItem)))
				.build();
	}
}
