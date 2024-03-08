package br.com.livelo.orderflight.enuns;

import br.com.livelo.orderflight.exception.OrderFlightException;
import lombok.Getter;

import java.util.Arrays;

import static br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR;

@Getter
public enum Partner {
    CVC("cvc_flight", "cvc_flight_tax");

    private final String skuFlight;
    private final String skuTax;

    Partner(String skuFlight, String skuTax) {
        this.skuFlight = skuFlight;
        this.skuTax = skuTax;
    }

    public static Partner findByName(String name) {
        return Arrays.stream(Partner.values())
                .filter(partner -> name.toUpperCase().equals(partner.name()))
                .findFirst()
                .orElseThrow(() -> new OrderFlightException(ORDER_FLIGHT_INTERNAL_ERROR, null, "Partner not found!"));
    }
}
