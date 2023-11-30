package br.com.livelo.br.com.livelo.components;

import br.com.livelo.br.com.livelo.dto.ProductDTO;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

class MessageReceiverTest {

    private final MessageReceiver messageReceiver = new MessageReceiver();

    @Test
    void testReceiveMessage() {
        ProductDTO productDTO = mock(ProductDTO.class);
        messageReceiver.receiveMessage(productDTO);

        verify(productDTO).getName();
    }

}
