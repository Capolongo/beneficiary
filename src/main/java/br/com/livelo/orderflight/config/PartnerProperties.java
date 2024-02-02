package br.com.livelo.orderflight.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "partner")
public class PartnerProperties {
    private Map<String, String> urls;

    public String getUrlByPartnerCode(String partnerCode) {
        return urls.get(partnerCode);
    }

}
