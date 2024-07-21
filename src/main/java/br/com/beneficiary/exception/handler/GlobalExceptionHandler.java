package br.com.recipient.exception.handler;

import br.com.recipient.dto.response.MessageResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleInvalidFormatException(InvalidFormatException ex) {
        String message = "Invalid value for enum: " + ex.getValue() +
                ". Accepted values are: " + Arrays.toString(ex.getTargetType().getEnumConstants());
        return ResponseEntity.badRequest().body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), message));
    }
}
