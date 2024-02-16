package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(OrderFlightException e) {
        var message = ofNullable(e.getArgs()).orElse(e.getMessage());
        ofNullable(e.getOrderFlightErrorType().getLevel()).ifPresent(level -> this.logMessage(level, message, e.getOrderFlightErrorType(), e));

        return ResponseEntity.status(e.getOrderFlightErrorType().getStatus())
                .body(this.buildError(e.getOrderFlightErrorType()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException e) {
        var message = String.format("Required header %s is missing!", e.getHeaderName());
        this.logMessage(Level.ERROR, message, OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e);

        return ResponseEntity.status(400)
                .body(this.buildError(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR));
    }

    private ErrorResponse buildError(OrderFlightErrorType orderFlightErrorType) {
        return new ErrorResponse(orderFlightErrorType.getCode(), orderFlightErrorType.getTitle(),
                orderFlightErrorType.getDescription());
    }

    private void logMessage(Level levelLog, String message, OrderFlightErrorType orderFlightErrorType, Exception e) {
        log.atLevel(levelLog).log("errorType: {} message: {}", orderFlightErrorType.getCode(), message, e);
    }
}
