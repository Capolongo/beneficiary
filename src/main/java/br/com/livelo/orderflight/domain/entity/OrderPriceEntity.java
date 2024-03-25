package br.com.livelo.orderflight.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_PRICE")
@EqualsAndHashCode(callSuper = false)
@ToString
public class OrderPriceEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_PRICE_SEQ")
    @SequenceGenerator(name = "ORDERS_PRICE_SEQ", sequenceName = "ORDERS_PRICE_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private Double accrualPoints; //RECUPERAR DA PRECIFICAÇÃO
    private BigDecimal amount; //RECUPERAR DA PRECIFICAÇÃO
    private BigDecimal pointsAmount; //RECUPERAR DA PRECIFICAÇÃO
    private BigDecimal partnerAmount;
    private String priceListId;
    private String priceListDescription;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_PRICE_ID")
    private Set<OrderPriceDescriptionEntity> ordersPriceDescription;
}
