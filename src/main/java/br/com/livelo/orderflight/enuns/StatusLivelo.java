package br.com.livelo.orderflight.enuns;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusLivelo {
    PROCESSING("LIVPNR-1007", "PROCESSING"),
    FAILED("LIVPNR-1014", "FAILED"),
    INITIAL("LIVPNR-1006", "INITIAL");

    private final String code;
    private final String description;
}
