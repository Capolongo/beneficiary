package br.com.livelo.orderflight.exception;

public record ErrorResponse(String code, String title, String description) {
}
