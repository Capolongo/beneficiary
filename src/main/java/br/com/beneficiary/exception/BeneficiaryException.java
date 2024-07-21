package br.com.recipient.exception;

import lombok.Getter;

@Getter
public class BeneficiaryException extends RuntimeException {

    public BeneficiaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeneficiaryException(String message) {
        super(message);
    }
}

