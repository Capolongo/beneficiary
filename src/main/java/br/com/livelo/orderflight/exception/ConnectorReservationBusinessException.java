package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationBusinessException extends ReservationException {
    public ConnectorReservationBusinessException(String args, Throwable e) {
        super(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, null, args, e);
    }
}
