package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import lombok.Getter;

@Getter
public class CartException extends RuntimeException {
    private final String args;
    private final CartErrorType cartErrorType;

    public CartException(CartErrorType cartErrorType, String message, String args, Throwable cause) {
        super(message, cause);
        this.args = args;
        this.cartErrorType = cartErrorType;
    }
}
