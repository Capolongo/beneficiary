package br.com.beneficiary.exception;

import br.com.beneficiary.exception.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    public void testHandleInvalidFormatException() throws Exception {
        mockMvc.perform(get("/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // A simple controller for testing purposes
    @RestController
    static class TestController {
        @GetMapping("/test")
        public void testEndpoint() throws InvalidFormatException {
            throw new InvalidFormatException(null, "Invalid value", "invalidValue", TestEnum.class);
        }
    }

    enum TestEnum {
        VALUE1, VALUE2;
    }
}