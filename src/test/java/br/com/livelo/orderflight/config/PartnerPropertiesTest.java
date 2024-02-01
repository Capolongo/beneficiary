package br.com.livelo.orderflight.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.livelo.orderflight.configs.PartnerProperties;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
