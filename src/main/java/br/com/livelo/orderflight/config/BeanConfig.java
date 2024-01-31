package br.com.livelo.orderflight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}
