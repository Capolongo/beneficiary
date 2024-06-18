package br.com.livelo.orderflight.domain.dto.reservation.connector;

public record ConnectorErrorResponse(String code, String message, ConnectorDetailsErrorResponse details) {
}
