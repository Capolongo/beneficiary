package br.com.livelo.orderflight.domain.dtos.headers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.livelo.orderflight.constants.HeadersConstants;

@ExtendWith(MockitoExtension.class)
class RequiredHeadersTest {

    @Test
    void buildObject() {
        RequiredHeaders object = new RequiredHeaders("transactionId", "userId");
        assertNotNull(object);
    }

    @Test
    void shouldGetAllRequiredHeaders() {
        String transactionId = "transactionId";
        String userId = "userId";

        Map<String, String> mockHeaders = new HashMap<>();
        mockHeaders.put(HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, transactionId);
        mockHeaders.put(HeadersConstants.LIVELO_USER_ID_HEADER, userId);

        RequiredHeaders requiredHeaders = new RequiredHeaders(transactionId, userId);
        Map<String, String> headers = requiredHeaders.getAllRequiredHeaders();
        assertEquals(headers, mockHeaders);
    }
}