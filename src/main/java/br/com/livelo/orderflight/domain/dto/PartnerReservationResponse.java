package br.com.livelo.orderflight.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PartnerReservationResponse {
	private String commerceOrderId;
	private String partnerOrderId;
    private String partnerCode;
    private LocalDateTime expirationDate;
    private String createDate;
    private String status;
    private BigDecimal amount;
    private List<PartnerReservationOrdersPriceDescription> ordersPriceDescription;
    private List<PartnerReservationItem> items;
}
