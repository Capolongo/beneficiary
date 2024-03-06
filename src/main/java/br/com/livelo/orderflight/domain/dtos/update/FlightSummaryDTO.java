package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class FlightSummaryDTO {

    private ValidatingAirlineDTO airline;
    private DepartureArrivalDTO departure;
    private DepartureArrivalDTO arrival;
    private Integer duration;
    private BaggageDTO baggage;
    private List<ServiceDTO> services;
    private List<CustomerDTO> passengers;
    private List<LegSummaryDTO> legs;
    private GlobalDistribuitionSystemDTO globalDistribuitionSystem;
    private String route;
    private String onLineCheckinLink;
    @Deprecated
    private Boolean isFlexible;

}