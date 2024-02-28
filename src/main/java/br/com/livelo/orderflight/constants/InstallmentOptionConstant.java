package br.com.livelo.orderflight.constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "fixed-values.installment-option")
public class InstallmentOptionConstant implements Serializable {
    private String id;
    private String currency;
    private BigDecimal interest;
    private Integer parcels;
}
