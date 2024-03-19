package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.livelo.orderflight.constants.DynatraceConstants.*;
import static java.util.Optional.ofNullable;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(OrderFlightException.class)
    public ResponseEntity<ErrorResponse> handleException(OrderFlightException e) {
        setDynatraceEntries(e);

        var message = ofNullable(e.getArgs()).orElse(e.getMessage());
        ofNullable(e.getOrderFlightErrorType().getLevel()).ifPresent(level -> this.logMessage(level, message, e.getOrderFlightErrorType(), e));
        MDC.clear();

        return ResponseEntity.status(e.getOrderFlightErrorType().getStatus())
                .body(this.buildError(e.getOrderFlightErrorType()));
    }

    private void setDynatraceEntries(OrderFlightException e) {
        MDC.put(STATUS, "ERROR");
        MDC.put(ERROR_CATEGORY, e.getOrderFlightErrorType().getCategory());
        MDC.put(ERROR_TYPE, e.getOrderFlightErrorType().name());
        MDC.put(ERROR_MESSAGE, e.getArgs());
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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getFieldErrors();
        List<Map<String, String>> errors = fieldErrors.stream()
                .map(fieldError -> {
                    Map<String, String> errorDetails = new HashMap<>();
                    errorDetails.put("field", fieldError.getField());
                    errorDetails.put("message", fieldError.getDefaultMessage());
                    return errorDetails;
                })
                .toList();

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<Map<String, String>>> getErrorsMap(List<Map<String, String>> errors) {
        Map<String, List<Map<String, String>>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
