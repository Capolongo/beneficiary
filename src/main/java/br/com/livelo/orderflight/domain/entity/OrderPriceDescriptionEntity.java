package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_PRICE_DESCRIPTION")
public class OrderPriceDescriptionEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_PRICE_DESCRIPTION_SEQ")
    @SequenceGenerator(name = "ORDERS_PRICE_DESCRIPTION_SEQ", sequenceName = "ORDERS_PRICE_DESCRIPTION_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private BigDecimal amount;

    private BigDecimal pointsAmount;

    private String type;

    private String description;
}
