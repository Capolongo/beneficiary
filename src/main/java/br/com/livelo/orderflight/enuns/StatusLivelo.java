package br.com.livelo.orderflight.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusLivelo {
    PROCESSING("LIVPNR-1007", "PROCESSING"),
    FAILED("LIVPNR-1014", "FAILED"),
    INITIAL("LIVPNR-1006", "INITIAL"),
    CANCELED("LIVPNR-9001", "CANCELED"),
    COMPLETED("LIVPNR-1011", "COMPLETED"),
    VOUCHER("LIVPNR-1019", "VOUCHER");

    private final String code;
    private final String description;
}
