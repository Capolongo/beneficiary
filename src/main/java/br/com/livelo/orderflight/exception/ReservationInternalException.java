package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ReservationInternalException extends RuntimeException{
    private final String args;
    private final ReservationErrorType reservationErrorType;
    public ReservationInternalException(String args) {

        super(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR.getDescription());

        this.args = args;
        this.reservationErrorType = ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR;
    }
}
