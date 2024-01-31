package br.com.livelo.orderflight.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PartnerPropertiesTest {

    @Test
    void shoultGetUrl() {
        var expected = "http://test";
        var partner = "CVC";
        Map<String, String> urls = new HashMap<>();
        urls.put(partner, expected);

        var properties = new PartnerProperties();
        properties.setUrls(urls);

        var response = properties.getUrlByPartnerCode(partner);
        assertEquals(expected, response);
    }
    
    
    @Test
    void shoultGetExpirationTimer() {
        var expected = 15;
        var partner = "CVC";
        Map<String, Long> expirationTimers = new HashMap<>();
        expirationTimers.put(partner, (long) expected);

        var properties = new PartnerProperties();
        properties.setExpirationTimer(expirationTimers);

        var response = properties.getExpirationTimerByParterCode(partner);
        assertEquals(expected, response);
    }
}
