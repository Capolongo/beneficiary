package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import lombok.Getter;

@Getter
public class CartException extends RuntimeException {
    private final String args;
    private final Exception e;
    private final CartErrorType cartErrorType;

    public CartException(CartErrorType cartErrorType, String message, String args, Exception e) {
        super(message);
        this.args = args;
        this.e = e;
        this.cartErrorType = cartErrorType;
    }
}
