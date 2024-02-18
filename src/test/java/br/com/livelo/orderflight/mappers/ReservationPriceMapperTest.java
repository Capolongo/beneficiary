package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.*;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationPriceMapperTest {
    ReservationPriceMapper mapper = new ReservationPriceMapperImpl();

    @Test
    void shouldReturnOrdersPriceDescription() {
        var response = this.mapper.mapOrdersPriceDescription(PartnerReservationResponse.builder()
                .ordersPriceDescription(
                        List.of(PartnerReservationOrdersPriceDescription.builder()
                                .amount(new BigDecimal(10))
                                .type("BY_ADULT")
                                .taxes(
                                        List.of(PartnerReservationOrdersPriceDescriptionTaxes.builder()
                                                .amount(new BigDecimal(10))
                                                .description("TESTE_TAX")
                                                .build())).build())
                ).build(),
                PricingCalculatePrice.builder().priceListId("price")
                        .pricesDescription( new ArrayList<>(List.of(PricingCalculatePricesDescription.builder()
                                .passengerType("BY_ADULT").pointsAmount(7)
                                .taxes(new ArrayList<>(List.of(PricingCalculateTaxes.builder()
                                        .description("TESTE_TAX")
                                                .pointsAmount(7)
                                        .build())))
                                .build()))).build());
        assertNotNull(response);
    }
}
