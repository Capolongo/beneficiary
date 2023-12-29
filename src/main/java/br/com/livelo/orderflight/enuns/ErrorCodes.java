package br.com.livelo.orderflight.enuns;

public enum ErrorCodes {
    INTERNAL_SERVER_ERROR("Internal server error"),
    INVALID_REQUEST("Invalid request"),
    VALIDATION_FAILED("Validation failed"),

    USER_NOT_FOUND("User not found");
    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}