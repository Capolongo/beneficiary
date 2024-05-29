package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationInternalException extends OrderFlightException {
    public ConnectorReservationInternalException(OrderFlightErrorType errorType, String args, Throwable e) {
        super(errorType, null, args, e);
    }
}
