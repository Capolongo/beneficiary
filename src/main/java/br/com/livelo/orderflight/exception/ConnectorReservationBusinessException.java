package br.com.livelo.orderflight.exception;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.Getter;

@Getter
public class ConnectorReservationBusinessException extends OrderFlightException {
    public ConnectorReservationBusinessException(String args, Throwable e) {
        super(OrderFlightErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, null, args, e);
    }
}
