package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.Getter;

@Getter
public class OrderFlightException extends RuntimeException {
    private final String args;
    private final OrderFlightErrorType orderFlightErrorType;

    public OrderFlightException(OrderFlightErrorType orderFlightErrorType, String message, String args, Throwable cause) {
        super(message, cause);
        this.args = args;
        this.orderFlightErrorType = orderFlightErrorType;
    }

    public OrderFlightException(OrderFlightErrorType orderFlightErrorType, String message, String args) {
        super(message);
        this.args = args;
        this.orderFlightErrorType = orderFlightErrorType;
    }
}

