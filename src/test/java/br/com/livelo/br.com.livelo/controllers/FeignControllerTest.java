package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.client.JsonPlaceHolderClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class FeignControllerTest {

    private final JsonPlaceHolderClient jsonPlaceHolderClient = Mockito.mock(JsonPlaceHolderClient.class);
    private final FeignController feignController = new FeignController(jsonPlaceHolderClient);

    @Test
    void testGetMethod() {
        feignController.getTodos();
        verify(jsonPlaceHolderClient).getTodos();
    }
}
