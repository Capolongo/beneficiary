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
@ConfigurationProperties(prefix = "fixed-values.shipment-option")
public class ShipmentOptionConstant implements Serializable {
    private Long id;
    private String currency;
    private String description;
    private BigDecimal price;
    private String type;
}
