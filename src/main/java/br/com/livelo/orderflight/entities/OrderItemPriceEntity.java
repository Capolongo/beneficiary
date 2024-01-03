package br.com.livelo.orderflight.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "ORDERS_ITEM_PRICE")
public class OrderItemPriceEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERS_ITEM_PRICE_SEQ")
    @SequenceGenerator(name = "ORDERS_ITEM_PRICE_SEQ", sequenceName = "ORDERS_ITEM_PRICE_SEQ", allocationSize = 1)
    @Id
    private Long id;

    private String listPrice;

    private BigDecimal amount;

    private BigDecimal pointsAmount;

    private BigDecimal accrualPoints;

    private BigDecimal partnerAmount;

    private String priceListId;

    @Lob
    private String priceRule;
}
