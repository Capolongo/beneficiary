package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class PartnerReservationResponse {
	private String commerceOrderId;
	private String partnerOrderId;
    private String partnerCode;
    private LocalDateTime expirationDate;
    private String createDate;
    private PartnerResponseStatus status;
    private BigDecimal amount;
    private List<PartnerReservationOrdersPriceDescription> ordersPriceDescription;
    private List<PartnerReservationItem> items;
}
