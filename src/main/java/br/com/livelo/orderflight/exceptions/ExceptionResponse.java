package br.com.livelo.orderflight.exceptions;


import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> details;

    public ExceptionResponse(final HttpStatus httpStatus, String details) {
        this.code = httpStatus.name();
        this.message = httpStatus.getReasonPhrase();
        this.details = Collections.singletonList(details);
    }

}