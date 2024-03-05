package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationItem;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateFlight;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculatePrice;
import br.com.livelo.orderflight.domain.dtos.pricing.response.PricingCalculateTaxes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ReservationItemPriceMapperTest {

    private ReservationItemPriceMapper mapper = new ReservationItemPriceMapperImpl();
    String FLIGHT_TYPE = "type_flight";
    String TAX_TYPE = "type_flight_tax";
    @Test
    void shouldPointsAmountFlight() {
        var partnerReservationItem = PartnerReservationItem.builder().type(FLIGHT_TYPE).build();
        var pricingCalculatePrice = PricingCalculatePrice.builder().flight(
                PricingCalculateFlight.builder().pointsAmount(BigDecimal.TWO).build()
        ).build();
        var response = this.mapper.pointsAmount(partnerReservationItem,pricingCalculatePrice);
        assertEquals(BigDecimal.TWO, response);
    }

    @Test
    void shouldPointsAmountTaxes() {
        var partnerReservationItem = PartnerReservationItem.builder().type(TAX_TYPE).build();
        var pricingCalculatePrice = PricingCalculatePrice.builder().taxes(
                PricingCalculateTaxes.builder().pointsAmount(BigDecimal.ONE).build()
        ).build();
        var response = this.mapper.pointsAmount(partnerReservationItem,pricingCalculatePrice);
        assertEquals(BigDecimal.ONE, response);
    }

    @Test
    void shouldAmountFlight() {
        var partnerReservationItem = PartnerReservationItem.builder().type(FLIGHT_TYPE).build();
        var pricingCalculatePrice = PricingCalculatePrice.builder().flight(
                PricingCalculateFlight.builder().amount(BigDecimal.TWO).build()
        ).build();
        var response = this.mapper.amount(partnerReservationItem,pricingCalculatePrice);
        assertEquals(BigDecimal.TWO, response);
    }

    @Test
    void shouldAmountTaxes() {
        var partnerReservationItem = PartnerReservationItem.builder().type(TAX_TYPE).build();
        var pricingCalculatePrice = PricingCalculatePrice.builder().taxes(
                PricingCalculateTaxes.builder().amount(BigDecimal.TEN).build()
        ).build();
        var response = this.mapper.amount(partnerReservationItem,pricingCalculatePrice);
        assertEquals(BigDecimal.TEN, response);
    }
}
