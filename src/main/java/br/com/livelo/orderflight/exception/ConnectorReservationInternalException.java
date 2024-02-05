package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationInternalException extends ReservationException{
    public ConnectorReservationInternalException(String args, Throwable e) {
        super(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, null, args, e);
    }
}
