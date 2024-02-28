package br.com.livelo.orderflight.utils;

import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.Message;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class MessageUtils {
    public static String extractBodyAsString(Message message) {
        byte[] bodyAsBytes = message.getBody();
        return new String(bodyAsBytes, UTF_8);
    }
}
