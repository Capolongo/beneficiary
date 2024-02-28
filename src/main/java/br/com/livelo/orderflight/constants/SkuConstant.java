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
@ConfigurationProperties(prefix = "fixed-values.sku")
public class SkuConstant implements Serializable {
    private BigDecimal listPrice;
    private BigDecimal salePrice;
    private Integer quantity;
    private String currency;
    private String description;
    private Boolean available;
}
