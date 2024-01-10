package br.com.livelo.orderflight.utils;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


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
