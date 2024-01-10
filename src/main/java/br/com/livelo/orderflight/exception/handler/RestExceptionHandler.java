package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.CartException;
import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Optional.ofNullable;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(CartException ex) {
        ofNullable(ex.getArgs()).ifPresent(log::error);
        return ResponseEntity.status(ex.getCartErrorType().getStatus()).body(this.buildError(ex.getCartErrorType()));
    }

    private ErrorResponse buildError(CartErrorType cartErrorType) {
        return new ErrorResponse(cartErrorType.getCode(), cartErrorType.getTitle(), cartErrorType.getDescription());
    }
}
