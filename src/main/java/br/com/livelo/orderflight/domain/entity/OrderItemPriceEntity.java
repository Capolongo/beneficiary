package br.com.livelo.orderflight.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_ITEM_PRICE")
public class OrderItemPriceEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_ITEM_PRICE_SEQ")
    @SequenceGenerator(name = "ORDERS_ITEM_PRICE_SEQ", sequenceName = "ORDERS_ITEM_PRICE_SEQ", allocationSize = 1)
    @Id
    private Long id;
    private String listPrice;
    private BigDecimal amount; // RECUPERAR NA PRECIFICAÇÃO
    private BigDecimal pointsAmount; // RECUPERAR NA PRECIFICAÇÃO
    private BigDecimal accrualPoints; // RECUPERAR NA PRECIFICAÇÃO
    private BigDecimal partnerAmount;
    private String priceListId;
    @Lob
    private String priceRule;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ITEM_PRICE_ID")
    private List<PriceModalityEntity> pricesModalities;
}
