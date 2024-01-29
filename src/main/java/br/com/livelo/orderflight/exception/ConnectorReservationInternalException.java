package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationInternalException extends RuntimeException{
    private final String args;
    private final ReservationErrorType reservationErrorType;
    public ConnectorReservationInternalException(String args) {

        super(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR.getDescription());

        this.args = args;
        this.reservationErrorType = ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR;
    }
}
