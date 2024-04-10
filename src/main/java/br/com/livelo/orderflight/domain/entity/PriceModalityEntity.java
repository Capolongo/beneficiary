package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRICE_MODALITY")
@EqualsAndHashCode(callSuper = false)
@ToString
public class PriceModalityEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRICE_MODALITY_SEQ")
    @SequenceGenerator(name = "PRICE_MODALITY_SEQ", sequenceName = "PRICE_MODALITY_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private BigDecimal amount;
    private BigDecimal pointsAmount;
    private Float multiplier;
    private Float multiplierAccrual;
    private Float markup;
    private Double accrualPoints;
    private String priceListId;
}
