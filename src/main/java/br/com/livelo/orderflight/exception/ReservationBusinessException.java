package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ReservationBusinessException extends RuntimeException {
    private final String args;
    private final ReservationErrorType reservationErrorType;
    public ReservationBusinessException(String args) {
        super(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR.getDescription());

        this.args = args;
        this.reservationErrorType = ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR;
    }
}
