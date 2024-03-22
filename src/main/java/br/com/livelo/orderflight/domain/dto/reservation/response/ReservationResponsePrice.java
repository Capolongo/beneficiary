package br.com.livelo.orderflight.domain.dto.reservation.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ReservationResponsePrice (Double accrualPoints,
										BigDecimal amount,
										BigDecimal pointsAmount,
										BigDecimal partnerAmount,
										Float pointsMultiplier,
										Float markup,
										Double accrualMultiplier,
										String priceListId,
										Set<ReservationResponsePriceDescription> ordersPriceDescription,
										List<ReservationResponsePriceModality> pricesModalities){

}
