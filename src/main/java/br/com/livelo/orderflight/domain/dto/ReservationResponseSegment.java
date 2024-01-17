package br.com.livelo.orderflight.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;

import br.com.livelo.orderflight.domain.entity.CancelationRuleEntity;
import br.com.livelo.orderflight.domain.entity.ChangeRuleEntity;
import br.com.livelo.orderflight.domain.entity.FlightLegEntity;
import br.com.livelo.orderflight.domain.entity.LuggageEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

public record ReservationResponseSegment(String partnerId,
										 String step,
										 Integer stops,
										 Integer flightDuration,
										 String originIata,
										 String originDescription,
										 String destinationIata,
										 String destinationDescription,
										 LocalDateTime departureDate,
										 LocalDateTime arrivalDate,
										 Set<ReservationResponseLuggage> luggages,
										 Set<ReservationResponseCancelationRule> cancelationRules,
										 Set<ReservationResponseChangeRule> changeRules,
										 Set<ReservationResponseFlightLeg> flightsLegs){

}
