package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerReservationResponse {
	private String commerceOrderId;
	private String partnerOrderId;
    private String partnerCode;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime expirationDate;
    private String createDate;
    private PartnerResponseStatus currentStatus;
    private BigDecimal amount;
    private PartnerReservationOrdersPriceDescription ordersPriceDescription;
    private List<PartnerReservationItem> items;
}
