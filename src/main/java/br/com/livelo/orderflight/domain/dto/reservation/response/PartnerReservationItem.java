package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationItem {
    private String type;
    private Integer quantity;
    private BigDecimal listPrice;
    private BigDecimal amount;
    private PartnerReservationTravelInfo travelInfo;
    private List<PartnerReservationSegment> segments;
    private String productId;
}
