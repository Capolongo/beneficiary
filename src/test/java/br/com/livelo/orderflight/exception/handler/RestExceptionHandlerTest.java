package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.OrderFlightException;
import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {
    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    void shouldReturnErrorResponse() {
        var exception = new OrderFlightException(OrderFlightErrorType.ORDER_FLIGHT_INTERNAL_ERROR, "message",
                "message");

        var response = this.restExceptionHandler.handleException(exception);
        assertAll(
                () -> assertTrue(response.getStatusCode().is5xxServerError()),
                () -> assertInstanceOf(ErrorResponse.class, response.getBody()));
    }

    @Test
    void shouldReturnErrorResponse_WhenMissingHeader() {
        var exception = mock(MissingRequestHeaderException.class);

        var response = this.restExceptionHandler.handleException(exception);
        assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertInstanceOf(ErrorResponse.class, response.getBody()));
    }

    @Test
    void shouldReturnErrorResponse_WhenDocumentIsNotValid() {
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("document", "documentNumber", "Campo documentNumber não pode ser vazio"));
        fieldErrors.add(new FieldError("document", "type", "Error message 2"));

        MethodArgumentNotValidException notValidExample = new MethodArgumentNotValidException(null, new BeanPropertyBindingResult(new Object(), "objectName"));

        for (FieldError fieldError : fieldErrors) {
            notValidExample.getBindingResult().addError(fieldError);
        }

        ResponseEntity<Map<String, List<Map<String, String>>>> responseEntity = restExceptionHandler.handleValidationErrors(notValidExample);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, List<Map<String, String>>> responseBody = responseEntity.getBody();

        assert responseBody != null;
        assertEquals(2, responseBody.get("errors").size());
        assertEquals("documentNumber", responseBody.get("errors").getFirst().get("field"));
        assertEquals("Campo documentNumber não pode ser vazio", responseBody.get("errors").getFirst().get("message"));
    }
}
