package br.com.livelo.orderflight.configs.order.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusConstants {
    PROCESSING("LIVPNR-1007", "PROCESSING"),
    FAILED("LIVPNR-1014", "FAILED"),
    INITIAL("LIVPNR-1006", "INITIAL");

    private final String code;
    private final String description;
}