package br.com.livelo.orderflight.domain.entity;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS_PRICE")
public class OrderPriceEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_PRICE_SEQ")
    @SequenceGenerator(name = "ORDERS_PRICE_SEQ", sequenceName = "ORDERS_PRICE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private Double accrualPoints; //TODO RECUPERAR DA PRECIFICAÇÃO

    private BigDecimal amount; //TODO RECUPERAR DA PRECIFICAÇÃO

    private BigDecimal pointsAmount; //TODO RECUPERAR DA PRECIFICAÇÃO

    private BigDecimal partnerAmount; //TODO RECUPERAR DO RESPONSE DO CONECTOR

    private String priceListId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_PRICE_ID")
    private Set<OrderPriceDescriptionEntity> ordersPriceDescription;

}
