package br.com.livelo.orderflight.config;

import br.com.livelo.orderflight.exception.ConnectorReservationInternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RetryConditionEvaluator {

    private final PartnerProperties partnerProperties;
    private final RetryTemplate retryTemplate;

    public RetryTemplate createRetryTemplate(String partnerCode) {

        RetryPolicy retryPolicy = new SimpleRetryPolicy(
                partnerProperties.getAttemptByPartnerCode(partnerCode),
                Collections.singletonMap(ConnectorReservationInternalException.class, true)
        );

        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
