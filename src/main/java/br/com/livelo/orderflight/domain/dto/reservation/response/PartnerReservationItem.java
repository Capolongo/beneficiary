package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PartnerReservationItem {
    private String type;
    private Integer quantity;
    private BigDecimal listPrice;
    private BigDecimal amount;
    private PartnerReservationTravelInfo travelInfo;
    private List<PartnerReservationSegment> segments;
    private String productId;
}
