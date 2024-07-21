package br.com.beneficiary.exception.handler;

import br.com.beneficiary.dto.response.MessageResponse;
import br.com.beneficiary.exception.BeneficiaryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(BeneficiaryException.class)
    public ResponseEntity<MessageResponse> handleException(BeneficiaryException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
