package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(ReservationException ex) {
        ofNullable(ex.getArgs()).ifPresent(log::error);
        return ResponseEntity.status(ex.getReservationErrorType().getStatus()).body(this.buildError(ex.getReservationErrorType()));
    }

    private ErrorResponse buildError(ReservationErrorType reservationErrorType) {
        return new ErrorResponse(reservationErrorType.getCode(), reservationErrorType.getTitle(), reservationErrorType.getDescription());
    }
}
