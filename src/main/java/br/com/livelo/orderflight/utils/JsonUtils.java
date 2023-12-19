package br.com.livelo.orderflight.utils;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtils {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method responsible to convert an object into string json
     */
    public String convert(Object obj) {
        try {
            Assert.notNull(obj, "parameter obj cannot be null");
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.error("JsonUtils.convert - Error - exception.messages: {}", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
