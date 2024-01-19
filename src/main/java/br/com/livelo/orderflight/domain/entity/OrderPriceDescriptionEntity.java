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
@Table(name = "ORDERS_PRICE_DESCRIPTION")
@EqualsAndHashCode(callSuper = false)
public class OrderPriceDescriptionEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_PRICE_DESCRIPTION_SEQ")
    @SequenceGenerator(name = "ORDERS_PRICE_DESCRIPTION_SEQ", sequenceName = "ORDERS_PRICE_DESCRIPTION_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private BigDecimal amount; //RECUPERAR DA PRECIFICAÇÃO

    private BigDecimal pointsAmount; //RECUPERAR DA PRECIFICAÇÃO

    private String type;

    private String description;
}
