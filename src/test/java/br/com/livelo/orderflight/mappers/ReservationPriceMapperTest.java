package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionFlight;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePricesDescription;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationPriceMapperTest {
    ReservationPriceMapper mapper = Mappers.getMapper(ReservationPriceMapper.class);

    @Test
    void shouldReturnOrdersPriceDescription() {
        var response = this.mapper.mapOrdersPriceDescription(PartnerReservationResponse.builder()
                        .ordersPriceDescription(
                                PartnerReservationOrdersPriceDescription.builder()
                                        .flights(List.of(
                                                PartnerReservationOrdersPriceDescriptionFlight.builder()
                                                        .amount(new BigDecimal(10))
                                                        .passengerType("BY_ADULT")
                                                        .build()
                                        ))
                                        .taxes(
                                                List.of(PartnerReservationOrdersPriceDescriptionTaxes.builder()
                                                        .amount(new BigDecimal(10))
                                                        .type("TESTE_TAX")
                                                        .build())).build()
                        ).build());
        assertNotNull(response);
    }
}
