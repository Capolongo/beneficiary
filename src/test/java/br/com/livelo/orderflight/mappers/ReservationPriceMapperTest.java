package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescription;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationOrdersPriceDescriptionTaxes;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReservationPriceMapperTest {
    ReservationPriceMapper mapper = new ReservationPriceMapperImpl();

    @Test
    void shouldReturnOrdersPriceDescription() {
        var response = this.mapper.mapOrdersPriceDescription(PartnerReservationResponse.builder()
                .ordersPriceDescription(List.of(
                        PartnerReservationOrdersPriceDescription.builder()
                                .taxes(
                                        List.of(PartnerReservationOrdersPriceDescriptionTaxes.builder().build())
                                )
                                .build()
                )).build(),
                PricingCalculatePrice.builder().build());
        assertNotNull(response);
    }
}
