package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(ReservationException e) {
        var message = ofNullable(e.getArgs()).orElse(e.getMessage());
        ofNullable(e.getReservationErrorType().getLevel()).ifPresent(level -> this.logMessage(level, message, e));

        return ResponseEntity.status(e.getReservationErrorType().getStatus())
                .body(this.buildError(e.getReservationErrorType()));
    }

    private ErrorResponse buildError(ReservationErrorType reservationErrorType) {
        return new ErrorResponse(reservationErrorType.getCode(), reservationErrorType.getTitle(),
                reservationErrorType.getDescription());
    }

    private void logMessage(Level levelLog, String message, ReservationException e) {
        log.atLevel(levelLog).log("errorType: {} message: {}", e.getReservationErrorType().getCode(), message, e);
    }
}
