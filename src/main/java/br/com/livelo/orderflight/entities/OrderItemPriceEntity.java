package br.com.livelo.orderflight.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_ITEM_PRICE")
public class OrderItemPriceEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_ITEM_PRICE_SEQ")
    @SequenceGenerator(name = "ORDER_ITEM_PRICE_SEQ", sequenceName = "ORDER_ITEM_PRICE_SEQ", allocationSize = 1)
    @Column(name = "ID")
    @Id
    private Integer id;
    @Column(name = "LIST_PRICE")
    private String listPrice;
    @Column(name = "AMOUNT")
    private Integer amount;
    @Column(name = "POINTS_AMOUNT")
    private BigDecimal pointsAmount;
    @Column(name = "ACCRUAL_POINTS")
    private BigDecimal accrualPoints;
    @Column(name = "PARTNER_AMOUNT")
    private BigDecimal partnerAmount;
    @Column(name = "PRICE_LIST_ID")
    private String priceListId;
    @Lob
    @Column(name = "PRICE_RULE")
    private String priceRule;
    @CreationTimestamp
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;
}
