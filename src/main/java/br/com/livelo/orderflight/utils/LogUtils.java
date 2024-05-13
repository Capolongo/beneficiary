package br.com.livelo.orderflight.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LogUtils {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String writeAsJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("error on creating object {} log", object.getClass().getSimpleName(), e);
            return object.toString();
        }
    }
}
