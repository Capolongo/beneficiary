package br.com.livelo.orderflight.utils;

import br.com.livelo.orderflight.exception.enuns.OrderFlightErrorType;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

import static br.com.livelo.orderflight.constants.DynatraceConstants.*;
import static java.util.Optional.ofNullable;

@UtilityClass
public class DynatraceUtils {

    public static void setDynatraceErrorEntries(Map<String, String> entries) {
        MDC.put(ERROR_CATEGORY, entries.get(ERROR_CATEGORY));
        MDC.put(ERROR_TYPE, entries.get(ERROR_TYPE));
        MDC.put(ERROR_MESSAGE, entries.get(ERROR_MESSAGE));
    }

    public static Map<String, String> buildEntries(OrderFlightErrorType errorType, String errorMessage) {
        Map<String, String> entries = new HashMap<>();
        entries.put(ERROR_CATEGORY, errorType.getCategory());
        entries.put(ERROR_TYPE, ofNullable(errorType.getDetailedMessage()).orElse(errorType.name()));
        entries.put(ERROR_MESSAGE, errorMessage);
        return entries;
    }
}
