package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ReservationException extends RuntimeException {
    private final String args;
    private final ReservationErrorType reservationErrorType;

    public ReservationException(ReservationErrorType reservationErrorType, String message, String args, Throwable cause) {
        super(message, cause);
        this.args = args;
        this.reservationErrorType = reservationErrorType;
    }

    public ReservationException(ReservationErrorType reservationErrorType, String message, String args) {
        super(message);
        this.args = args;
        this.reservationErrorType = reservationErrorType;
    }

}
