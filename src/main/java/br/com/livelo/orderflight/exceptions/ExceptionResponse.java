package br.com.livelo.orderflight.exceptions;


import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import br.com.livelo.orderflight.contants.ErrorCodes;
import lombok.Getter;

@Getter
public class ExceptionResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> details;

    public ExceptionResponse(final ErrorCodes errorCode, String details) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.details = Collections.singletonList(details);
    }

}