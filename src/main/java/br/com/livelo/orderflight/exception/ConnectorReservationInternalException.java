package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationInternalException extends OrderFlightException {
    public ConnectorReservationInternalException(String args, Throwable e) {
        super(OrderFlightErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, null, args, e);
    }
}
