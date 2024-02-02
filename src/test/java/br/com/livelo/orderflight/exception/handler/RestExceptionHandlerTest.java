package br.com.livelo.orderflight.exception.handler;

import br.com.livelo.orderflight.exception.ErrorResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {
    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    void shouldReturnErrorResponse() {
        var exception = new ReservationException(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, "message",
                "message");

        var response = this.restExceptionHandler.handleException(exception);
        assertAll(
                () -> assertTrue(response.getStatusCode().is5xxServerError()),
                () -> assertInstanceOf(ErrorResponse.class, response.getBody()));
    }
}
