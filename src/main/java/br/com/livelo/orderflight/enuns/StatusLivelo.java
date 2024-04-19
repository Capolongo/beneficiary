package br.com.livelo.orderflight.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusLivelo {
    INITIAL("LIVPNR-1006", "INITIAL"),
    PROCESSING("LIVPNR-1007", "PROCESSING"),
    FAILED("LIVPNR-1014", "FAILED"),
    WAIT_VOUCHER("LIVPNR-1019", "PROCESSING"),
    VOUCHER_SENT("LIVPNR-1030", "PROCESSING"),
    COMPLETED("LIVPNR-1011", "COMPLETED"),
    CANCELED("LIVPNR-9001", "CANCELED");

    private final String code;
    private final String description;
}
