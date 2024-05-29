package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationBusinessException extends OrderFlightException {
    public ConnectorReservationBusinessException(OrderFlightErrorType errorType, String args, Throwable e) {
        super(errorType, null, args, e);
    }
}
