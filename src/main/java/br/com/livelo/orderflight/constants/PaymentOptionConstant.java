package br.com.livelo.orderflight.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "fixed-values.payment-option")
public class PaymentOptionConstant implements Serializable {
    private String id;
    private String description;
    private String name;
}
